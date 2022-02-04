package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.home.Product
import com.example.acedrops.repository.home.ProductsRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductsRepository) : ViewModel() {

    private var _productList: MutableLiveData<ApiResponse<OneCategoryResult>> = MutableLiveData()
    val productList: LiveData<ApiResponse<OneCategoryResult>>
        get() = _productList

    fun getProductList(categoryName: String) = viewModelScope.launch {
        _productList = repository.getProductList(categoryName)
    }

    private var _wishlist: MutableLiveData<ApiResponse<List<Product>>> = MutableLiveData()
    val wishlist: LiveData<ApiResponse<List<Product>>>
        get() = _wishlist

    fun getWishlist() = viewModelScope.launch {
        _wishlist = repository.getWishlist()
    }
}
