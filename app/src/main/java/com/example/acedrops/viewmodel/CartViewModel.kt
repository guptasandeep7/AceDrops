package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.repository.dashboard.CartRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    private var _cartData: MutableLiveData<ApiResponse<CartData>> = MutableLiveData()
    val cartData: LiveData<ApiResponse<CartData>>
        get() = _cartData

    var totalAmount = MutableLiveData<Long>(0)

    fun getCartData() = viewModelScope.launch {
        _cartData = repository.getCartList()
    }

    private var _atcResult: MutableLiveData<ApiResponse<CartResponse>> = MutableLiveData()
    val atcResult: LiveData<ApiResponse<CartResponse>>
        get() = _atcResult

    fun increaseQuantity(productId: String) = viewModelScope.launch {
        _atcResult = repository.addToCart(productId)
    }

    private var _removeFromCartResult: MutableLiveData<ApiResponse<CartResponse>> =
        MutableLiveData()
    val removeFromCartResult: LiveData<ApiResponse<CartResponse>>
        get() = _removeFromCartResult

    fun decreaseQuantity(productId: String) = viewModelScope.launch {
        _removeFromCartResult = repository.removeFromCart(productId)
    }

    private var _wishlistResult: MutableLiveData<ApiResponse<WishlistResponse>> = MutableLiveData()
    val wishlistResult: LiveData<ApiResponse<WishlistResponse>>
        get() = _wishlistResult

    fun addWishlist(productId: String) = viewModelScope.launch {
        _wishlistResult = repository.addRemoveWishlist(productId)
    }

    init {
        getCartData()
    }
}