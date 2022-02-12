package com.example.acedrops.viewmodel

import android.content.Context
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

    var totalAmount = MutableLiveData<Long>(0)

    private var cartData: MutableLiveData<ApiResponse<CartData>>? = null

    fun getCartData(context: Context): MutableLiveData<ApiResponse<CartData>>? {
        viewModelScope.launch {
            cartData = CartRepository().getCartList(context)
        }
        return cartData
    }

    var atcResult: MutableLiveData<ApiResponse<CartResponse>> = MutableLiveData()

    fun increaseQuantity(productId: String, context: Context): MutableLiveData<ApiResponse<CartResponse>> {
        viewModelScope.launch {
            atcResult = CartRepository().addToCart(productId, context)
        }
        return atcResult
    }

    private var removeFromCartResult: MutableLiveData<ApiResponse<CartResponse>> =
        MutableLiveData()

    fun decreaseQuantity(productId: String, context: Context): MutableLiveData<ApiResponse<CartResponse>> {
        viewModelScope.launch {
            removeFromCartResult = CartRepository().removeFromCart(productId, context)
        }
        return removeFromCartResult
    }

    var deleteFromCartResult: MutableLiveData<ApiResponse<CartResponse>> =
        MutableLiveData()

    fun deleteProduct(productId: String, context: Context): MutableLiveData<ApiResponse<CartResponse>> {
        viewModelScope.launch {
            deleteFromCartResult = CartRepository().deleteFromCart(productId, context)
        }
        return deleteFromCartResult
    }

    private var wishlistResult: MutableLiveData<ApiResponse<WishlistResponse>> = MutableLiveData()

    fun addWishlist(productId: String, context: Context): MutableLiveData<ApiResponse<WishlistResponse>> {
        viewModelScope.launch {
            wishlistResult = CartRepository().addRemoveWishlist(productId, context)
        }
        return wishlistResult
    }

}