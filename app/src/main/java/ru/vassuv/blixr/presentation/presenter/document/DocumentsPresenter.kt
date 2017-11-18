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
import ru.vassuv.blixr.App
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.document.DocumentsView


@InjectViewState
class DocumentsPresenter : MvpPresenter<DocumentsView>() {

    lateinit var simpleFragmentPagerAdapter: SimpleFragmentPagerAdapter

    fun onCreate(fragmentManager: FragmentManager) {
        simpleFragmentPagerAdapter = SimpleFragmentPagerAdapter(App.context, fragmentManager)
    }


    fun getPagerAdapter() = simpleFragmentPagerAdapter
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

    var adapter = DocumentAdapter(mapOf(1 to "pyxtybt",
            2 to "pysfffslflxtybt",
            3 to "dfgjskfgls",
            4 to "sfadfhadadfjfjadfjfjfj"))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recycler_layout, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return rootView
    }
}

class SimpleFragmentPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount() = 3

    override fun getItem(position: Int) = when (position) {
        1 -> ListDocumentsFragment()
        2 -> ListDocumentsFragment()
        else -> ListDocumentsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> mContext.getString(R.string.tab_page_1)
        1 -> mContext.getString(R.string.tab_page_2)
        2 -> mContext.getString(R.string.tab_page_3)
        else -> null
    }
}