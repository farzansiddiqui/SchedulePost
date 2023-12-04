package com.siddiqui.schedulepost.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.siddiqui.schedulepost.R
import com.siddiqui.schedulepost.databinding.FragmentPostBinding
import com.siddiqui.schedulepost.view.MainActivity


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPostBtn.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

    }


}