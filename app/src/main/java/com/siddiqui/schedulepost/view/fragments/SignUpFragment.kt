package com.siddiqui.schedulepost.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.siddiqui.schedulepost.databinding.FragmentSignUpBinding
import com.siddiqui.schedulepost.tool.UserManager
import com.siddiqui.schedulepost.tool.ValidEmail

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var fragmentContext: Context
    lateinit var userManager:UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManager = UserManager(requireActivity().supportFragmentManager)

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

        // Registration the user if they have not register.
        binding.signUpBtn.setOnClickListener {
            if (binding.editTextName.text.toString()
                    .isNotEmpty() && binding.editTextPassword.text.toString().isNotEmpty()
                && binding.editTextEmailOrMobile.text.toString().isNotEmpty()
            ) {
                if (ValidEmail().isValidEmail(binding.editTextEmailOrMobile.text.toString())) {
                    userManager.duplicateValue(
                        fragmentContext,
                        binding.editTextName.text.toString(),
                        binding.editTextEmailOrMobile.text.toString(),
                        binding.editTextPassword.text.toString()
                    )


                } else {
                    Toast.makeText(context, "Please correct email..", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please enter the all the text field", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }


}