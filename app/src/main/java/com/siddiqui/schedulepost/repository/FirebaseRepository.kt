package com.siddiqui.schedulepost.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.siddiqui.schedulepost.model.UserRegistration
import com.siddiqui.schedulepost.view.ListPostActivity

class FirebaseRepository {

    private val databaseReference:DatabaseReference = Firebase.database.reference

    fun writeDataToDataBase(userRegistration: UserRegistration){
        databaseReference.child("users").child(databaseReference.
        child("users").push().key ?: "").setValue(userRegistration)
    }
    fun readDataFromDataBase(context:Context,email:String, password:String){
        val emailQuery = databaseReference.child("users").orderByChild("emailOrMobile").equalTo(email)
        emailQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var emailAndPasswordExist = false
                for (dataSnapShotChild in snapshot.children){
                    val userRegistration = dataSnapShotChild.getValue(UserRegistration::class.java)
                    // Check if the password matches for any user with the specified email
                    if (userRegistration?.password.equals(password)) {
                        emailAndPasswordExist = true
                        break
                    }

                }
                if (emailAndPasswordExist){
                   context.startActivity(Intent(context, ListPostActivity::class.java))

                }else {
                    Toast.makeText(context, "Data doesn't exists.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}