package com.siddiqui.schedulepost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.model.GridImageView
import com.siddiqui.schedulepost.viewmodel.GridViewModel

class GridViewAdapter(
    private val context: Context,
    private val data: LiveData<ArrayList<GridImageView>>,
    private var arrayList: ArrayList<GridViewModel>
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return arrayList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): GridImageView {
        return data.value?.get(position)
    }

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
        val gridViewModel = getItem(position)
        val setImageVIew = view?.findViewById<ImageView>(R.id.set_imageView)
        setImageVIew?.setImageURI(gridViewModel.)

        return view!!
    }
}