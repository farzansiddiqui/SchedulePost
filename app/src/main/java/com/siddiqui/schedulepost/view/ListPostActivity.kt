package com.siddiqui.schedulepost.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.tabs.TabLayoutMediator
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.adapter.ViewPagerAdapter
import com.siddiqui.schedulepost.databinding.ActivityListPostBinding

class ListPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
                when (position){
                    0 -> {
                        tab.text = getString(R.string.post_schedule)
                        tab.icon = AppCompatResources.getDrawable(this,com.facebook.R.drawable.com_facebook_button_icon)
                    }
                    1 -> {
                        tab.text = getString(R.string.scheduled_post)
                        tab.icon = AppCompatResources.getDrawable(this,R.drawable.baseline_calendar_month_24)
                    }
                }
        }.attach()
    }
}