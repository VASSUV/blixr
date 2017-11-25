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
import kotlinx.android.synthetic.main.fragment_documents.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_documents, container, false)
        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        if (viewPager.adapter == null)
            viewPager.adapter = mDocumentsPresenter.getPagerAdapter(childFragmentManager)
    }
}
