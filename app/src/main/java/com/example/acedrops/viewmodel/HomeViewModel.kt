package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.home.HomeFragmentData
import com.example.acedrops.repository.home.HomeRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var homeData = MutableLiveData<ApiResponse<HomeFragmentData>>()

    fun getHomeData(context: Context): MutableLiveData<ApiResponse<HomeFragmentData>> {
        viewModelScope.launch {
            homeData = HomeRepository().getData(context)
        }
        return homeData
    }

}