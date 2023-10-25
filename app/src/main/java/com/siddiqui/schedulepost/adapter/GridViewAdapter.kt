package com.siddiqui.schedulepost.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.model.GridImageView
import com.siddiqui.schedulepost.viewmodel.GridViewModel

class GridViewAdapter(
    private val context: Context,
    private val imageUrs:MutableList<Uri?>
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return  imageUrs.size
    }

    // below function is use to return the item of grid view)
    override fun getItem(position: Int) = imageUrs[position]

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView

        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            view = layoutInflater?.inflate(R.layout.add_image_grid, parent, false)
        }


        val setImageView = view?.findViewById<ImageView>(R.id.set_imageView)
        val addImageView = view?.findViewById<ImageView>(R.id.add_imageView)
        val clearButton = view?.findViewById<ImageView>(R.id.clear_imageView)
        val addPostTextView = view?.findViewById<TextView>(R.id.textView)
        val uri = getItem(position)

        if (uri != null){
            setImageView?.setImageURI(uri)
            clearButton?.visibility = View.VISIBLE
            addImageView?.visibility = View.GONE
            addPostTextView?.visibility = View.GONE

        }else {
            setImageView?.setImageURI(null)
            clearButton?.visibility = View.GONE
            addImageView?.visibility = View.VISIBLE
            addPostTextView?.visibility = View.VISIBLE

        }

        return view!!
    }
}