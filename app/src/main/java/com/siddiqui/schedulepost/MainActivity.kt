package com.siddiqui.schedulepost

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.siddiqui.schedulepost.adapter.GridViewAdapter
import com.siddiqui.schedulepost.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val imageUris = mutableListOf<Uri?>(null)
    private lateinit var adapter: GridViewAdapter
    private var nextDefaultItemPosition = 0 // Track the position for the next default item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = GridViewAdapter(this, imageUris)
        binding.gridView.adapter = adapter

        val photoPicker = PhotoPicker(this) { uri ->
            // Set the selected image URI at the next default item position
            imageUris[imageUris.indexOf(null)] = uri
            adapter.notifyDataSetChanged()

            if (imageUris.contains(uri)){
                nextDefaultItemPosition++
                    imageUris.add(nextDefaultItemPosition, null)
                    adapter.notifyDataSetChanged()

            }

            // Increment the position for the next default item


            // Add a new default item in the list for the next image
         /*   if (nextDefaultItemPosition < imageUris.size){
                imageUris.add(nextDefaultItemPosition, null)
                Log.d("TAG", "position : $nextDefaultItemPosition")
                adapter.notifyDataSetChanged()

            }*/
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            if (imageUris[position] == null) {
                photoPicker.pickMedia()

            }
        }


    }


}