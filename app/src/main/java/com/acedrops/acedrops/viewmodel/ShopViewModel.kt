package com.acedrops.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.home.ShopResult
import com.acedrops.acedrops.repository.home.ShopRepository
import com.acedrops.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class ShopViewModel(val repository: ShopRepository, val shopId: Int) : ViewModel() {

    private var _shopDetails:MutableLiveData<ApiResponse<ShopResult>> = MutableLiveData()
    val shopDetails:LiveData<ApiResponse<ShopResult>>
    get() = _shopDetails

    fun getShopDetails(shopId: Int) = viewModelScope.launch {
        _shopDetails = repository.getShopDetails(shopId)
    }

    init {
        getShopDetails(shopId)
    }

}
