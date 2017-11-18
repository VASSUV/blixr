package ru.vassuv.blixr.ui.fragment.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.document.DocumentsView
import ru.vassuv.blixr.presentation.presenter.document.DocumentsPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import android.support.design.widget.TabLayout
import ru.vassuv.blixr.presentation.presenter.document.SimpleFragmentPagerAdapter
import android.support.v4.view.ViewPager



class DocumentsFragment() : MvpAppCompatFragment(), DocumentsView, IFragment {

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
        mDocumentsPresenter.onCreate(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_documents, container, false)
        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = mDocumentsPresenter.getPagerAdapter()
        tabLayout.setupWithViewPager(viewPager)
        return rootView
    }
}
