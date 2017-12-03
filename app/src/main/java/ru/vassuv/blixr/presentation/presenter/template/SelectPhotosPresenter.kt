package ru.vassuv.blixr.presentation.presenter.template

import android.app.Activity


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.SelectPhotosView
import android.graphics.Bitmap
import android.os.Handler
import android.view.*
import android.widget.*
import android.widget.AbsListView.MultiChoiceModeListener
import ru.vassuv.blixr.App
import ru.vassuv.blixr.ui.activity.MainActivity
import java.io.IOException

@InjectViewState
class SelectPhotosPresenter : MvpPresenter<SelectPhotosView>() {

    var adapter: GridViewAdapter? = null
    var pictures = arrayOfNulls<Bitmap?>(0)

    fun getGridAdapter(context: Context): GridViewAdapter {
        if (adapter == null)
            adapter = GridViewAdapter(context, R.layout.item_grid_layout, ElectronicTemplatePresenter.photo)
        return adapter!!
    }

    inner class GridViewAdapter(
            context: Context,
            private val layoutResourceId: Int,
            var data: ArrayList<Uri> = arrayListOf()
    ) : ArrayAdapter<Uri>(context, layoutResourceId, data) {

        init {
            pictures = arrayOfNulls<Bitmap?>(data.size)
        }

        override fun notifyDataSetChanged() {
            pictures = arrayOfNulls<Bitmap?>(data.size)
            super.notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var row = convertView
            var holder = ViewHolder()
            if (row == null) {
                val inflater = (context as Activity).layoutInflater
                row = inflater.inflate(layoutResourceId, parent, false)
                holder.imageTitle = row.findViewById(R.id.text)
                holder.image = row.findViewById(R.id.image)
                row.tag = holder
            } else {
                holder = row.tag as ViewHolder
            }

            val item = data[position]
            holder.imageTitle!!.text = item.lastPathSegment

            val picture = pictures[position] ?: getBitmapFromUri(data[position])
            pictures[position] = picture
            holder.image!!.setImageBitmap(picture)

            return row!!
        }

        inner class ViewHolder {
            var imageTitle: TextView? = null
            var image: ImageView? = null
        }

    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor = App.context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    fun getAddClickListener() = View.OnClickListener {
        // todo permission !!!

        viewState.launchGallery(299)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                299 -> resultData?.data?.let { addPicture(it) }
            }
        }
    }

    private fun addPicture(uri: Uri) {
        ElectronicTemplatePresenter.photo.add(uri)
        ElectronicTemplatePresenter.photo.distinctBy { it.lastPathSegment }
        adapter?.data = ElectronicTemplatePresenter.photo
        adapter?.notifyDataSetChanged()
    }

    fun getMultiChoiceListener(activity: MainActivity): MultiChoiceModeListener = object : MultiChoiceModeListener {
        val selectedItems = arrayListOf<Int>()

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            selectedItems.sortDescending()
            selectedItems.forEach {
                ElectronicTemplatePresenter.photo.removeAt(it)
            }
            adapter?.data = ElectronicTemplatePresenter.photo
            adapter?.notifyDataSetChanged()
            mode?.finish()
            return true
        }

        override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
            if (checked) {
                val pos = selectedItems.indexOf(position)
                if (pos == -1) {
                    selectedItems.add(position)
                }
            } else {
                selectedItems.remove(position)
            }
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.multichoice_pictures, menu)
            activity.hideActionBar()
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onDestroyActionMode(mode: ActionMode?) {
            Handler().postDelayed({activity.showActionBar()}, 300)
        }
    }
}
