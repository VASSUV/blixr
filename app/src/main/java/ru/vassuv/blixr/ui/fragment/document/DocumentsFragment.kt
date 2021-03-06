package ru.vassuv.blixr.ui.fragment.document

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.document.DocumentsView
import ru.vassuv.blixr.presentation.presenter.document.DocumentsPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.*
import kotlinx.android.synthetic.main.fragment_documents.*
import ru.vassuv.blixr.runBg
import ru.vassuv.blixr.ui.activity.LoginActivity
import ru.vassuv.blixr.ui.components.SystemState
import ru.vassuv.blixr.utils.ATLibriry.Router

class DocumentsFragment : MvpAppCompatFragment(), DocumentsView, IFragment {
    @InjectPresenter
    lateinit var mDocumentsPresenter: DocumentsPresenter

    override val type: FragmentFabric = FrmFabric.DOCUMENTS

    companion object {

        fun newInstance(): DocumentsFragment {
            val fragment = DocumentsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_documents, container, false)
        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)
        println("onCreateView")
        return rootView
    }

    override fun onStart() {
        super.onStart()
        viewPager.adapter = mDocumentsPresenter.getPagerAdapter(childFragmentManager)
        mDocumentsPresenter.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_autorized, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.search -> {
            Router.navigateTo(FrmFabric.SEARCH.name)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        mDocumentsPresenter.onStop()
    }
}
