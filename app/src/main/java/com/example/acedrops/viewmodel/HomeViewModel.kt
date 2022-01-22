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

    private var _homeData = MutableLiveData<ApiResponse<HomeFragmentData>>()
    val homeData: LiveData<ApiResponse<HomeFragmentData>>
    get() = _homeData

    fun getHomeData() = viewModelScope.launch {
        _homeData = homeRepository.getData()
    }

    init {
        getHomeData()
    }
}