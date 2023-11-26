package com.siddiqui.schedulepost.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siddiqui.schedulepost.repository.FirebaseRepository

class TestViewModel:ViewModel() {
    private val mutableLiveData = MutableLiveData<String>()
    private val repository = FirebaseRepository()
    val liveData:LiveData<String> get() = mutableLiveData




    fun fetchData(){
        val newData = "Fetch Data from source"
        mutableLiveData.value = newData
    }



}