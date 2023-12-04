package com.siddiqui.schedulepost.tool
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class PhotoPicker(
    private val activity: AppCompatActivity,
    private val onMediaPicked: (Uri?) -> Unit
) {
    private val pickMedia =
        activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onMediaPicked(uri)
            } else {
                onMediaPicked(null)

                Toast.makeText(activity, "No Image Select", Toast.LENGTH_SHORT).show()
            }

        }


    fun pickMedia() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }


}


