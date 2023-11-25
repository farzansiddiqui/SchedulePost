package com.siddiqui.schedulepost.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.databinding.FragmentSignUpBinding
import com.siddiqui.schedulepost.model.UserRegistration

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {
            if (binding.editTextName.text.toString()
                    .isNotEmpty() && binding.editTextPassword.text.toString().isNotEmpty()
                && binding.editTextEmailOrMobile.text.toString().isNotEmpty()
            ) {
                if (isValidEmail(binding.editTextEmailOrMobile.text.toString())) {
                    val userData = UserRegistration(binding.editTextName.text.toString(),
                        binding.editTextEmailOrMobile.text.toString(), binding.editTextPassword.text.toString())
                        //userRegistration(userData)
                             duplicateValue(binding.editTextEmailOrMobile.text.toString())
                         navigateToLoginFragment()

                } else {
                    Toast.makeText(context, "Please correct email..", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please enter the all the text field", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun navigateToLoginFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_ContainerView, SignFragment()).commit()
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("^(([\\w-.]+)@([\\w-]+\\.)+([a-z]{2,}))")
        return regex.matches(email)

    }
    private fun userRegistration(userRegistration: UserRegistration) {
        database.child("users").child(database.child("users").push().key ?: "").setValue(userRegistration)
    }
    private fun duplicateValue(email:String){
        val query = database.child("users").orderByChild("emailOrMobile").equalTo(email)

        query.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    // email already exits in the database.
                    // Handle the case where the email is already registered.
                    Toast.makeText(context, "email already exists", Toast.LENGTH_SHORT).show()
                }else {
                    val userData = UserRegistration(binding.editTextName.text.toString(),
                        email, binding.editTextPassword.text.toString())
                    userRegistration(userData)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}