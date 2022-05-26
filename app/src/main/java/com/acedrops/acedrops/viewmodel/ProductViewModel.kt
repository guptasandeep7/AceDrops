package com.acedrops.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.allproducts.OneCategoryResult
import com.acedrops.acedrops.model.cart.WishlistResponse
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.model.productDetails.ProductDetails
import com.acedrops.acedrops.repository.ProductRepository
import com.acedrops.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ProductViewModel : ViewModel() {

    var product = MutableLiveData<Product>()
    var productDetails: MutableLiveData<ApiResponse<ProductDetails>> = MutableLiveData()
    var productList: MutableLiveData<ApiResponse<OneCategoryResult>> = MutableLiveData()
    var oneCategoryData: MutableLiveData<OneCategoryResult> = MutableLiveData()
    var wishlist: MutableLiveData<ApiResponse<List<Product>>> = MutableLiveData()
    var atcResult: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()
    var wishlistResult: MutableLiveData<ApiResponse<WishlistResponse>> = MutableLiveData()
    var result: MutableLiveData<ApiResponse<ResponseBody>> = MutableLiveData()

    fun addToCart(productId: Int, context: Context): MutableLiveData<ApiResponse<Boolean>> {
        viewModelScope.launch {
            atcResult = ProductRepository().addToCart(productId, context)
        }
        return atcResult
    }

    fun getProductDetails(
        productId: Int,
        context: Context
    ): MutableLiveData<ApiResponse<ProductDetails>> {
        viewModelScope.launch {
            productDetails = ProductRepository().getProductDetails(productId, context)
        }
        return productDetails
    }

    fun getProductList(
        categoryName: String,
        context: Context
    ): MutableLiveData<ApiResponse<OneCategoryResult>> {
        viewModelScope.launch {
            productList = ProductRepository().getProductList(categoryName, context)
        }
        return productList
    }

    fun addWishlist(
        productId: String,
        context: Context
    ): MutableLiveData<ApiResponse<WishlistResponse>> {
        viewModelScope.launch {
            wishlistResult = ProductRepository().addRemoveWishlist(productId, context)
        }
        return wishlistResult
    }

    fun getWishlist(context: Context): MutableLiveData<ApiResponse<List<Product>>> {
        viewModelScope.launch {
            wishlist = ProductRepository().getWishlist(context)
        }
        return wishlist
    }

    fun postReviewAndRating(
        prodId: Int,
        review: String,
        rating: String,
        context: Context
    ): MutableLiveData<ApiResponse<ResponseBody>> {
        viewModelScope.launch {
            result = ProductRepository().postReviewAndRating(prodId, review, rating, context)
        }
        return result
    }
}
