package com.siddiqui.schedulepost.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.databinding.ActivityUserLoginBinding
import com.siddiqui.schedulepost.view.fragments.SignFragment
import com.siddiqui.schedulepost.view.fragments.SignUpFragment

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            openFragment(SignFragment())
        }

        binding.signUpBtn.setOnClickListener {
            openFragment(SignUpFragment())
        }
    }

   private fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction  = fragmentManager.beginTransaction()
       transaction.replace(R.id.fragment_ContainerView, fragment).commit()

    }

}