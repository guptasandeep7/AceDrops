package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.home.Product
import com.example.acedrops.model.productDetails.ProductDetails
import com.example.acedrops.repository.ProductRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    var product = MutableLiveData<Product>()
    var productDetails: MutableLiveData<ApiResponse<ProductDetails>> = MutableLiveData()
    var productList: MutableLiveData<ApiResponse<OneCategoryResult>> = MutableLiveData()

    private var _atcResult: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()

    fun addToCart(productId: Int, context: Context): MutableLiveData<ApiResponse<Boolean>> {
        viewModelScope.launch {
            _atcResult = ProductRepository().addToCart(productId, context)
        }
        return _atcResult
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

}
