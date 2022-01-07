package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.home.HomeFragmentData
import com.example.acedrops.repository.dashboard.home.HomeRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    var homeData = MutableLiveData<ApiResponse<HomeFragmentData>>()

    fun getHomeData():LiveData<ApiResponse<HomeFragmentData>>{
        return homeData
    }

    init {
        viewModelScope.launch {
            homeData = homeRepository.getData()
        }
    }
}