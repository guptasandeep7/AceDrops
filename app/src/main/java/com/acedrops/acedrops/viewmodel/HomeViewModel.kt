package com.acedrops.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.home.HomeFragmentData
import com.acedrops.acedrops.repository.home.HomeRepository
import com.acedrops.acedrops.utill.ApiResponse
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