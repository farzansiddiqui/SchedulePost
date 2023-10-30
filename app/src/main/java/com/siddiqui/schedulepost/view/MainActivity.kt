package com.siddiqui.schedulepost.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.appevents.AppEventsLogger
import com.google.gson.JsonObject
import com.siddiqui.schedulepost.PhotoPicker
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.adapter.GridViewAdapter
import com.siddiqui.schedulepost.databinding.ActivityMainBinding
import com.siddiqui.schedulepost.retrofit.PostData
import com.siddiqui.schedulepost.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val imageUris = mutableListOf<Uri?>(null)
    private lateinit var adapter: GridViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                nextDefaultItemPosition++
                Log.d(TAG, "position:$nextDefaultItemPosition")
                adapter.notifyDataSetChanged()
            }
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            if (imageUris[position] == null) {
                photoPicker.pickMedia()
            }

        }
        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.post_item){
                Toast.makeText(this, "click post", Toast.LENGTH_SHORT).show()
            }
            true
        }
        //postData()
        newPostOnFacebook()

    }

    override fun onResume() {
        super.onResume()


    }

    companion object {
        const val TAG = "TAG"
        var nextDefaultItemPosition = 0
    }

    private fun newPostOnFacebook(){
        val accessToken = AccessToken.getCurrentAccessToken()
        Log.d(TAG, "newPostOnFacebook: $accessToken")
        val jsonObject = JSONObject()
        jsonObject.put("message","Testing API")

        val request = GraphRequest.newPostRequest(accessToken,"/114801539913429/feed",
            jsonObject
        ) { response ->
            if (response.error != null) {
                // Handle the error
                Log.e(TAG, "Error: ${response.error!!.errorMessage}")
            } else {
                // Post was successful
                val body = response.jsonObject
                Log.d(TAG, "Post successful. Response: $body")
            }
        }
        request.executeAsync()
    }

    private fun postData(){
        val pageId= "114801539913429"
        val message = "Testing the api to published on fb page"
        val accessToken = "EAAEwqSVvg2ABO5DvBazlP0njfd0aZCKWvI36M13aCHPvuUrBzPXMYNwXUi4e513eljD3DkuEVyWKIrLfGbimDJRLqD4szJZAEbtaj3nPa6myasBZBqe29RaW7uzckWUCOfg0AXREFnVZBvzoofV6YYM0Yu22uMIlToZCm0vgGahFZBqOqMZAap7fXTYmZCURERcLGBM0KPG0WWyJIqIZD"
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.apiInterface.publishedPost(pageId,message,"Bearer $accessToken")
            if (response.isSuccessful){
                Log.d(TAG, "successful: ${response.body()}")
            }else {
                val errorResponse = response.errorBody()?.string()
                Log.d(TAG, "Error: $errorResponse")
            }
        }
    }
}