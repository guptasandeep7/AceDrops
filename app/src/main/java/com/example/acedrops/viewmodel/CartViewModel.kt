package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.repository.dashboard.CartRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class CartViewModel(private val respository: CartRepository) : ViewModel() {

    private val _cartData: MutableLiveData<ApiResponse<ArrayList<Cart>>> = MutableLiveData()
    val cartData:LiveData<ApiResponse<ArrayList<Cart>>>
    get() = _cartData

    fun getCartData() = viewModelScope.launch {
            _cartData.value = respository.getCartList()
        }
}