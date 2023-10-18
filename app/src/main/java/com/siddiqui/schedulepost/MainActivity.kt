package com.siddiqui.schedulepost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.siddiqui.schedulepost.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picker = PhotoPicker(this){uri ->
            if (uri != null) {
               // binding.postImageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Select", Toast.LENGTH_SHORT).show()
            }
        }

    }


}