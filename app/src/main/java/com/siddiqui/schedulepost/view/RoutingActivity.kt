package com.siddiqui.schedulepost.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.databinding.ActivityRoutingBinding

class RoutingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition{
            true
        }
        val sharedPref = getSharedPreferences(packageName, MODE_PRIVATE) ?: return

        val userLogin = sharedPref.getBoolean(getString(R.string.userLogin), false)

        if (userLogin){
            startActivity(Intent(this@RoutingActivity, ListPostActivity::class.java))
        }else {
            startActivity(Intent(this@RoutingActivity, UserLoginActivity::class.java))
        }
        finish()
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}