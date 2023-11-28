package com.siddiqui.schedulepost.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.siddiqui.schedulepost.databinding.FragmentSignBinding
import com.siddiqui.schedulepost.model.UserRegistration
import com.siddiqui.schedulepost.view.ListPostActivity


class SignFragment : Fragment() {
    private lateinit var binding: FragmentSignBinding
    private lateinit var fragmentContext: Context
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInBtn.setOnClickListener {
            if (binding.edittextEmail.text.toString()
                    .isEmpty() && binding.editTextPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(fragmentContext, "Please enter the details", Toast.LENGTH_SHORT)
                    .show()
            } else {
                userLogin(
                    binding.edittextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    private fun userLogin(email: String, pass: String) {
        val emailQuery = database.child("users").orderByChild("emailOrMobile").equalTo(email)
        emailQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var emailAndPasswordExist = false
                for (dataSnapShotChild in snapshot.children) {
                    val userRegistration = dataSnapShotChild.getValue(UserRegistration::class.java)
                    // Check if the password matches for any user with the specified email
                    if (userRegistration?.password.equals(pass)) {
                        emailAndPasswordExist = true
                        break
                    }

                }
                if (emailAndPasswordExist) {
                    val sharedPref =
                        activity?.getSharedPreferences(context?.packageName, Context.MODE_PRIVATE)
                            ?: return
                    with(sharedPref.edit()) {
                        putBoolean(getString(R.string.userLogin), true)
                        apply()
                    }
                    startActivity(Intent(context, ListPostActivity::class.java))
                    activity?.finishAffinity()

                } else {
                    Toast.makeText(fragmentContext, "Data doesn't exists.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "failed: ${error.message}")
            }
        })
    }


}