package com.siddiqui.schedulepost.view

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.elevation.SurfaceColors
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.siddiqui.schedulepost.tool.PhotoPicker
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.adapter.GridViewAdapter
import com.siddiqui.schedulepost.databinding.ActivityMainBinding
import com.siddiqui.schedulepost.retrofit.RetrofitInstance
import com.siddiqui.schedulepost.tool.ImageCompressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val imageUris = mutableListOf<Uri?>(null)
    private lateinit var adapter: GridViewAdapter
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private var imageUriList = mutableListOf<Uri?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = SurfaceColors.getColorForElevation(this, 0f)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                // Push all content below the top system status bar
                topMargin = insets.top
                bottomMargin = insets.bottom

            }
            WindowInsetsCompat.CONSUMED
        }

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        adapter = GridViewAdapter(this, imageUris)
        binding.gridView.adapter = adapter


        val photoPicker = PhotoPicker(this) { uri ->
            // Set the selected image URI at the next default item position
            if (uri != null && uri != Uri.EMPTY) {
                imageUris.add(nextDefaultItemPosition, uri)
                imageUriList.add(uri)
                nextDefaultItemPosition++
                adapter.notifyDataSetChanged()
            }
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
                binding.enterEditTextCaptions.clearFocus()
            if (imageUris[position] == null) {
                photoPicker.pickMedia()
            }else {
                imageUris[position]?.let { openImageView( it) }
            }

        }
        //postData()

    }

    override fun onResume() {
        super.onResume()
        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.post_item) {
                if (binding.enterEditTextCaptions.text.toString()
                        .isNotEmpty() && nextDefaultItemPosition > 0
                ) {
                    if (imageUriList != Uri.EMPTY) {
                        CoroutineScope(Dispatchers.Main).launch {
                            uploadImage(imageUriList)

                        }
                        Toast.makeText(this, "Upload Image Successful!!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Please fill the both details", Toast.LENGTH_SHORT).show()
                }

            }
            true
        }

    }

    companion object {
        const val TAG = "TAG"
        var nextDefaultItemPosition = 0
    }

    private  fun uploadImage(imageUri: MutableList<Uri?>) {
        val compressQuality = 80
        val userId = "farzan";

        val compressor = ImageCompressor()
        for (imageList in imageUri) {
            val compressFile = compressor.compressImage(contentResolver,imageList!!,compressQuality)
            val file = Uri.fromFile(compressFile)
            val imageRef = storageRef.child("images/$userId"+System.currentTimeMillis()).putFile(file)
            imageRef.addOnSuccessListener {
                Log.d(TAG, "Image successful upload on firebase:")
            }.addOnCanceledListener {
                Log.d(TAG, "Image failed to upload try again!!.")
            }

        }
    }

    override fun onStop() {
        super.onStop()
        nextDefaultItemPosition = 0;
    }

    private fun postData() {
        val pageId = "114801539913429"
        val message = "Testing the api to published on fb page"
        val accessToken =
            "EAAEwqSVvg2ABO5DvBazlP0njfd0aZCKWvI36M13aCHPvuUrBzPXMYNwXUi4e513eljD3DkuEVyWKIrLfGbimDJRLqD4szJZAEbtaj3nPa6myasBZBqe29RaW7uzckWUCOfg0AXREFnVZBvzoofV6YYM0Yu22uMIlToZCm0vgGahFZBqOqMZAap7fXTYmZCURERcLGBM0KPG0WWyJIqIZD"
        CoroutineScope(Dispatchers.Main).launch {
            val response =
                RetrofitInstance.apiInterface.publishedPost(pageId, message, "Bearer $accessToken")
            if (response.isSuccessful) {
                val responseJson = response.body()
                if (responseJson != null) {
                    val pagId = responseJson.id
                    Log.d(TAG, "postData: $pagId")
                    Toast.makeText(
                        this@MainActivity,
                        "Message Post successful on facebook",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(TAG, "Response body is null or does not contain the ID.")

                }
            } else {
                val errorResponse = response.errorBody()?.string()
                Log.d(TAG, "Error: $errorResponse")
            }
        }
    }

    private fun openImageView(uri: Uri){
        val alertDialog = AlertDialog.Builder(this,R.style.TransparentAlertDialog)
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.image_dialog,null)
        val imageView:ImageView = view.findViewById(R.id.imageView_dialog)
        imageView.setImageURI(uri)
        alertDialog.setView(view)
        alertDialog.show()

    }


}