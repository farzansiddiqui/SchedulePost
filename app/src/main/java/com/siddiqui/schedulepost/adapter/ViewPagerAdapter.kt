package com.siddiqui.schedulepost.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.siddiqui.schedulepost.view.fragments.PostFragment
import com.siddiqui.schedulepost.view.fragments.ScheduledFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when (position) {
            0 -> PostFragment()
            1 -> ScheduledFragment()
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
    }

}