package ru.vassuv.blixr.presentation.presenter.document

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import ru.vassuv.blixr.App
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.presenter.document.DocumentsPresenter.DocumentType.*
import ru.vassuv.blixr.presentation.view.document.DocumentsView
import ru.vassuv.blixr.repository.ANY_PASSWORD
import ru.vassuv.blixr.repository.JSON_HEADER
import ru.vassuv.blixr.repository.SessionConfig
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.api.Methods.CONTRACT_DATA_BY_USER_ID
import ru.vassuv.blixr.repository.db.BPContractShort
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.USER_ID
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.repository.response.DocumentList
import ru.vassuv.blixr.repository.response.Token
import ru.vassuv.blixr.ui.components.SystemState
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult
import java.util.*

private var user: User? = null

@InjectViewState
class DocumentsPresenter : MvpPresenter<DocumentsView>() {
    var token: String = SharedData.TOKEN.getString()

    enum class DocumentType { BOUGHT, SOLD, DRAFT }

    val listFragments = arrayListOf(
            ListDocumentsFragment.newInstance(BOUGHT),
            ListDocumentsFragment.newInstance(SOLD),
            ListDocumentsFragment.newInstance(DRAFT))

    init {
        user = DataBase.getUser()
        if (user != null) {
            SystemState.loader?.visibility = true
            contractDataByUserId(user!!.id)
        }
    }

    private var pagerAdapter: SimpleFragmentPagerAdapter? = null

    fun getPagerAdapter(fragmentManager: FragmentManager): SimpleFragmentPagerAdapter? {
        if (pagerAdapter == null) {
            pagerAdapter = SimpleFragmentPagerAdapter(App.context, fragmentManager)
        }
        return pagerAdapter
    }

    private fun loadToken(restartMethod: (() -> Unit)? = null) {
        Methods.TOKEN.httpGet()
                .authenticate(SessionConfig.USER_NAME, SessionConfig.USER_PASSWORD)
                .responseObject(Token.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        token = verifyResult.value?.token ?: ""
                        SharedData.TOKEN.saveString(token)

                        if (restartMethod != null) {
                            restartMethod()
                        }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

    private fun contractDataByUserId(id: Int) {
        CONTRACT_DATA_BY_USER_ID.httpPost()
                .header(JSON_HEADER)
                .authenticate(token, ANY_PASSWORD)
                .body(JsonObject().add(USER_ID, id).toString())
                .responseObject(DocumentList.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        DataBase.saveDocuments(verifyResult.value) {
                            SystemState.loader?.visibility = false
                            pagerAdapter?.notifyDataSetChanged()
                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { contractDataByUserId(id) }
                    } else {
                        SystemState.loader?.visibility = false
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

    fun onStart() {
        SystemState.onNavigatorDragging = onNavigatorDragging
        SystemState.onNavigatorEdle = onNavigatorEdle
    }

    private val onNavigatorDragging = {

    }

    private val onNavigatorEdle = {
        if (!SystemState.isNavigatorVisible) {
            listFragments.forEach { it.updateViews() }
        }
    }

    fun onStop() {
        if (SystemState.onNavigatorDragging == onNavigatorDragging)
            SystemState.onNavigatorDragging = null
        if (SystemState.onNavigatorEdle == onNavigatorEdle)
            SystemState.onNavigatorEdle = null
    }

    inner class SimpleFragmentPagerAdapter(
            private val mContext: Context,
            fm: FragmentManager
    ) : FragmentPagerAdapter(fm) {

        override fun getCount() = 3

        override fun getItem(position: Int) = listFragments[position]

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            0 -> mContext.getString(R.string.my_documents_buy)
            1 -> mContext.getString(R.string.my_documents_sell)
            2 -> mContext.getString(R.string.my_documents_draft)
            else -> null
        }
    }
}

class ListDocumentsFragment : Fragment() {

    var type: DocumentsPresenter.DocumentType = BOUGHT

    companion object {
        fun newInstance(type: DocumentsPresenter.DocumentType): ListDocumentsFragment {
            val fragment = ListDocumentsFragment()
            fragment.type = type
            return fragment
        }
    }

    private var adapter: DocumentAdapter

    init {
        this.adapter = DocumentAdapter(arrayListOf())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recycler_layout, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return rootView
    }

    fun updateViews() {
        val method: (ArrayList<BPContractShort>) -> Unit = { documents ->
            adapter.list = documents
            adapter.notifyDataSetChanged()
        }
        when (type) {
            BOUGHT -> DataBase.getDocumentsBought(user?.id ?: -1, method)
            SOLD -> DataBase.getDocumentsSold(user?.id ?: -1, method)
            DRAFT -> DataBase.getDocumentsDraft(user?.id ?: -1, method)
        }
    }

    @JvmField
    val PLACEHOLDER_TYPE = 0

    @JvmField
    val ITEM_TYPE = 1

    inner class DocumentAdapter(var list: List<BPContractShort>)
        : RecyclerView.Adapter<DocumentAdapter.Holder>() {

        override fun getItemCount() = if (list.isEmpty()) 1 else list.size

        override fun getItemViewType(position: Int) = if (position == 0 && list.isEmpty()) PLACEHOLDER_TYPE else ITEM_TYPE

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = when (viewType) {
            PLACEHOLDER_TYPE -> Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_placeholder, parent, false))
            ITEM_TYPE -> DocumentHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_document, parent, false))
            else -> Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_placeholder, parent, false))
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            when (getItemViewType(position)) {
                ITEM_TYPE -> {
                    if (holder is DocumentHolder) {
                        val bpContract = list[position]
                        holder.title.text = "${bpContract.product} (${bpContract.contractCat})"
                        holder.price.text = App.context.getString(R.string.price_format, bpContract.contractPrice)
                        holder.date.text = bpContract.contractDate?.substring(0, 10) ?: ""
                        holder.seller.text = App.context.getString(R.string.seller, bpContract.seller ?: "")
                        holder.buyer.text = App.context.getString(R.string.buyer, bpContract.buyer ?: "")
                        holder.image.text = getContractUnicodeLogo(bpContract.contractCat ?: "")
                        holder.image.setTextColor(App.context.resources.getColor(
                                if(bpContract.buyer != null && bpContract.seller != null) R.color.textColorGreen else R.color.textColorSuperLight))
                    }
                }
            }
        }

        private fun getContractUnicodeLogo(contractCat: String) = when (contractCat) {
            "ELECTRONICS" -> "\uf287" // usb
            "MISC" -> "\uf0f6" // file
            "EVENT" -> "\uf145" // ticket
            "INSTRUMENTS" -> "\uf001" // music
//        "link" -> "\uf0c1" // link
//        "wrench" -> "\uf0ad" // wrench
//        "spinner" -> "\uf110" // spinner
            else -> "\uf287"
        }

        open inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

        inner class DocumentHolder(itemView: View) : Holder(itemView) {
            val price: TextView = itemView.findViewById(R.id.price)
            val title: TextView = itemView.findViewById(R.id.title)
            val image: TextView = itemView.findViewById(R.id.image)
            val date: TextView = itemView.findViewById(R.id.date)
            val buyer: TextView = itemView.findViewById(R.id.kopare)
            val seller: TextView = itemView.findViewById(R.id.saliare)

            init {
                itemView.setOnClickListener {

                }
            }
        }
    }
}