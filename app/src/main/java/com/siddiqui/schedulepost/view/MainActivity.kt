package com.siddiqui.schedulepost.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.elevation.SurfaceColors
import com.google.firebase.storage.FirebaseStorage
import com.siddiqui.schedulepost.PhotoPicker
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.adapter.GridViewAdapter
import com.siddiqui.schedulepost.databinding.ActivityMainBinding
import com.siddiqui.schedulepost.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val imageUris = mutableListOf<Uri?>(null)
    private lateinit var adapter: GridViewAdapter
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
  //  private val fileName = "image_${System.currentTimeMillis()}.jpg"
    private lateinit var imageUri: List<Uri>


    override fun onCreate(savedInstanceState: Bundle?) {
       enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = SurfaceColors.getColorForElevation(this,0f)


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
                imageUri = listOf(uri)
                nextDefaultItemPosition++
                adapter.notifyDataSetChanged()
            }
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            if (imageUris[position] == null) {
                photoPicker.pickMedia()
            }

        }
       //postData()

    }

    override fun onResume() {
        super.onResume()
        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.post_item){
                if (binding.enterEditTextCaptions.text.toString().isNotEmpty() && nextDefaultItemPosition > 0){
                    if (imageUri != Uri.EMPTY) {
                        CoroutineScope(Dispatchers.Main).launch {
                            uploadImage(imageUri)
                        }
                        Toast.makeText(this, "Upload Image", Toast.LENGTH_SHORT).show()
                    }

                }else {
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

    private fun uploadImage(imageUriList:List<Uri>){

        imageUriList.forEachIndexed{ index, uri ->
            val fileName = "image_$index${System.currentTimeMillis()}.jpg"

            val imageRef = storageRef.child("images/$fileName").putFile(uri)

            imageRef.addOnSuccessListener {
                Log.d(TAG, "Image successful upload on firebase: ")
            }.addOnCanceledListener {
                Log.d(TAG, "Retry: ")
            }
        }



    }

    override fun onStop() {
        super.onStop()
        nextDefaultItemPosition = 0;
    }

    private fun postData(){
        val pageId= "114801539913429"
        val message = "Testing the api to published on fb page"
        val accessToken = "EAAEwqSVvg2ABO5DvBazlP0njfd0aZCKWvI36M13aCHPvuUrBzPXMYNwXUi4e513eljD3DkuEVyWKIrLfGbimDJRLqD4szJZAEbtaj3nPa6myasBZBqe29RaW7uzckWUCOfg0AXREFnVZBvzoofV6YYM0Yu22uMIlToZCm0vgGahFZBqOqMZAap7fXTYmZCURERcLGBM0KPG0WWyJIqIZD"
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.apiInterface.publishedPost(pageId,message,"Bearer $accessToken")
            if (response.isSuccessful){
                    val responseJson = response.body()
                    if (responseJson != null) {
                        val pagId = responseJson.id
                        Log.d(TAG, "postData: $pagId")
                        Toast.makeText(this@MainActivity, "Message Post successful on facebook", Toast.LENGTH_SHORT).show()
                    }else{
                        Log.d(TAG, "Response body is null or does not contain the ID.")

                    }
                }else {
                val errorResponse = response.errorBody()?.string()
                Log.d(TAG, "Error: $errorResponse")
            }
        }
    }
}