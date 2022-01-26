package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.home.ShopResult
import com.example.acedrops.repository.dashboard.ProductRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    private var _atcResult: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()
    val atcResult: LiveData<ApiResponse<Boolean>>
    get() = _atcResult

    fun addToCart(productId: Int) = viewModelScope.launch {
        _atcResult = repository.addToCart(productId)
    }

}
