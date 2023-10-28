package com.siddiqui.schedulepost

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.size
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


        adapter = GridViewAdapter(this, imageUris)
        binding.gridView.adapter = adapter



        val photoPicker = PhotoPicker(this) { uri ->
            // Set the selected image URI at the next default item position
            if (uri != null && uri != Uri.EMPTY) {
              imageUris.add(nextDefaultItemPosition, uri)
                nextDefaultItemPosition++
                adapter.notifyDataSetChanged()
            }

        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            if (imageUris[position] == null) {
                photoPicker.pickMedia()
            }

        }


    }

    override fun onResume() {
        super.onResume()
        binding.postBtn.isEnabled = binding.gridView.size > 0 && binding.enterEditTextCaptions.text.toString().isNotEmpty()

    }

    companion object {
        const val TAG = "TAG"
        var nextDefaultItemPosition = 0
    }


}