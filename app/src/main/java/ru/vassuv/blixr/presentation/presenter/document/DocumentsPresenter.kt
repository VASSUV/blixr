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
import ru.vassuv.blixr.presentation.view.document.DocumentsView
import ru.vassuv.blixr.repository.ANY_PASSWORD
import ru.vassuv.blixr.repository.JSON_HEADER
import ru.vassuv.blixr.repository.SessionConfig
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.api.Methods.CONTRACT_DATA_BY_USER_ID
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.FETCH_URL
import ru.vassuv.blixr.repository.db.USER_ID
import ru.vassuv.blixr.repository.response.FetchBlocketAd
import ru.vassuv.blixr.repository.response.Token
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult


@InjectViewState
class DocumentsPresenter : MvpPresenter<DocumentsView>() {
    var token: String = ""
    val user = DataBase.getUser()

    fun getPagerAdapter(fragmentManager: FragmentManager) = SimpleFragmentPagerAdapter(App.context, fragmentManager)

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
                .responseObject(FetchBlocketAd.Deserializer()){ request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
//                        if (OK == verifyResult.status) {
//                            viewState.showSuccessAlert()
//                        } else {
//                            viewState.showErrorAlert()
//                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { contractDataByUserId(id) }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

    fun onStart() {
        if (user != null)
            contractDataByUserId(user.id)
    }

}




class DocumentAdapter(var list: Map<Int, String>) : RecyclerView.Adapter<DocumentAdapter.Holder>() {

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_document, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position + 1]
        holder.price.text = (position + 1).toString()
        holder.image.text = "\uf287"
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val price: TextView = itemView.findViewById(R.id.price)
        val title: TextView = itemView.findViewById(R.id.title)
        val image: TextView = itemView.findViewById(R.id.image)
    }
}

class ListDocumentsFragment : Fragment() {

    private var adapter = DocumentAdapter(mapOf(1 to "Contract 1",
            2 to "Contract 2",
            3 to "Contract 3",
            4 to "Contract 4"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recycler_layout, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return rootView
    }
}

class SimpleFragmentPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayListOf<ListDocumentsFragment>(ListDocumentsFragment(),
            ListDocumentsFragment(),
            ListDocumentsFragment())

    override fun getCount() = fragments.size

    override fun getItem(position: Int) = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> mContext.getString(R.string.my_documents_buy)
        1 -> mContext.getString(R.string.my_documents_sell)
        2 -> mContext.getString(R.string.my_documents_draft)
        else -> null
    }
}