package ru.vassuv.blixr.ui.fragment.template

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.SelectPhotosView
import ru.vassuv.blixr.presentation.presenter.template.SelectPhotosPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_select_photos.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import android.widget.GridView
import ru.vassuv.blixr.ui.activity.MainActivity


class SelectPhotosFragment : MvpAppCompatFragment(), SelectPhotosView, IFragment {
    override val type = FrmFabric.SELECT_PHOTOS

    companion object {

        fun newInstance(): SelectPhotosFragment {
            val fragment: SelectPhotosFragment = SelectPhotosFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    @InjectPresenter
    lateinit var presenter: SelectPhotosPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_photos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridView.adapter = presenter.getGridAdapter(context)
        gridView.choiceMode = GridView.CHOICE_MODE_MULTIPLE_MODAL
        gridView.setMultiChoiceModeListener(presenter.getMultiChoiceListener(activity as MainActivity))
        gridView.onItemLongClickListener = null
        add.setOnClickListener(presenter.getAddClickListener())
    }

    override fun launchGallery(READ_REQUEST_CODE: Int) {

        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        } else {
            TODO("VERSION.SDK_INT < KITKAT")
        }

        intent.addCategory(Intent.CATEGORY_OPENABLE)

        intent.type = "image/*"

        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  resultData: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, resultData)
    }

}
