package com.siddiqui.schedulepost.tool

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ImageCompressor {
    fun compressImage(contentResolver: ContentResolver,imageUri: Uri,quality:Int):File?{
        try {
            val inputStream:InputStream? = contentResolver.openInputStream(imageUri)
            // Decode the InputStream into a Bitmap
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            // Reset the InputStream and decode the image
            inputStream?.close()
            val newInputStream: InputStream? = contentResolver.openInputStream(imageUri)
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeStream(newInputStream, null, options)

            // Compress the Bitmap to reduce file size
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

            // Save the compressed Bitmap to a file
            val compressedFile = File.createTempFile("compressed_image", ".jpg")
            val fileOutputStream = FileOutputStream(compressedFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()

            return compressedFile
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null

    }
}