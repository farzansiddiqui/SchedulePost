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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picker = PhotoPicker(this) { uri ->
            if (uri != null) {
                // binding.postImageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Select", Toast.LENGTH_SHORT).show()
            }
        }
        adapter = GridViewAdapter(this, imageUris)
        binding.gridView.adapter = adapter

        val photoPicker = PhotoPicker(this) { uri ->
            imageUris[imageUris.indexOf(null)] = uri
            Log.d("TAG", "Image select")
            adapter.notifyDataSetChanged()
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            if (imageUris[position] == null) {
                photoPicker.pickMedia()
            }
        }


    }


}