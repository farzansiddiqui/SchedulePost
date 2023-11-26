package com.siddiqui.schedulepost.view

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
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}