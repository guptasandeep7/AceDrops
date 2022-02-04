package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.repository.CartRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private var _cartData: MutableLiveData<ApiResponse<CartData>>? = null

    fun getCartData(context: Context): MutableLiveData<ApiResponse<CartData>>? {
        viewModelScope.launch {
            _cartData = CartRepository().getCartList(context)
        }
        return _cartData
    }

    var totalAmount = MutableLiveData<Long>(0)

    private var _atcResult: MutableLiveData<ApiResponse<CartResponse>> = MutableLiveData()
    val atcResult: LiveData<ApiResponse<CartResponse>>
        get() = _atcResult

    fun increaseQuantity(productId: String, context: Context) = viewModelScope.launch {
        _atcResult = CartRepository().addToCart(productId, context)
    }

    private var _removeFromCartResult: MutableLiveData<ApiResponse<CartResponse>> =
        MutableLiveData()
    val removeFromCartResult: LiveData<ApiResponse<CartResponse>>
        get() = _removeFromCartResult

    fun decreaseQuantity(productId: String, context: Context) = viewModelScope.launch {
        _removeFromCartResult = CartRepository().removeFromCart(productId, context)
    }

    private var _deleteFromCartResult: MutableLiveData<ApiResponse<CartResponse>> =
        MutableLiveData()
    val deleteFromCartResult: LiveData<ApiResponse<CartResponse>>
        get() = _deleteFromCartResult

    fun deleteProduct(productId: String, context: Context) = viewModelScope.launch {
        _deleteFromCartResult = CartRepository().deleteFromCart(productId, context)
    }

    private var _wishlistResult: MutableLiveData<ApiResponse<WishlistResponse>> = MutableLiveData()
    val wishlistResult: LiveData<ApiResponse<WishlistResponse>>
        get() = _wishlistResult

    fun addWishlist(productId: String, context: Context) = viewModelScope.launch {
        _wishlistResult = CartRepository().addRemoveWishlist(productId, context)
    }

}