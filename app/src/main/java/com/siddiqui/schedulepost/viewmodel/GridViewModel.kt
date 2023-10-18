package com.siddiqui.schedulepost.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siddiqui.schedulepost.model.GridImageView

class GridViewModel:ViewModel() {
    private var lst = MutableLiveData<ArrayList<GridImageView>>()
   private var newLst = arrayListOf<GridImageView>()
    val data:LiveData<ArrayList<GridImageView>> = lst

   fun add(gridImageView: GridImageView){
       newLst.add(gridImageView)
       lst.value = newLst
   }
    fun remove(gridImageView: GridImageView){
        newLst.remove(gridImageView)
        lst.value = newLst
    }

}