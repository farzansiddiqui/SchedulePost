package com.siddiqui.schedulepost.tool

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.model.UserRegistration
import com.siddiqui.schedulepost.view.fragments.SignFragment
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserManager(val fragmentManager: FragmentManager) {

    // Function to create a new user and return the unique identifier
    fun createUser(userRegistration: UserRegistration): String {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        val userKey = usersRef.push().key


        usersRef.child(userKey ?: "").setValue(userRegistration)
        return userKey ?: ""
    }

    fun duplicateValue(context:Context,name:String,email:String,password:String) {
        val database = Firebase.database.reference
        val query = database.child("users").orderByChild("emailOrMobile").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // email already exits in the database.
                    // Handle the case where the email is already registered.

                    Toast.makeText(context, "email already exists", Toast.LENGTH_SHORT).show()

                } else {
                    val userData = UserRegistration(
                        name,
                        email, password
                    )
                    createUser(userData)
                 navigateToLoginFragment(fragmentManager)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }
        })
    }
    fun navigateToLoginFragment(fragmentManager: FragmentManager){
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_ContainerView, SignFragment()).commit()
    }

    suspend fun getUserEmail(email:String): String {
        val database = Firebase.database.reference
        val query = database.child("users").orderByChild("emailOrMobile").equalTo(email)
        return suspendCoroutine { continuation ->
            query.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        // Assuming there is only one user with the specified email.
                        val user = snapshot.children.first()
                        val userEmail = user.child("emailOrMobile").getValue(String::class.java)

                        continuation.resume(userEmail ?: "")
                    }else {
                        continuation.resume("")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })

        }
    }


}