package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.MyOrders
import com.example.acedrops.model.home.Product
import com.example.acedrops.repository.profile.OrderRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class OrderViewModel : ViewModel() {

    var product: Product? = null
    var quantity: String = ""
    var addressId: Int = 0
    var totalAmount:Long = 0
    var lastFragment:String?= null

    var orders: MutableLiveData<ApiResponse<List<MyOrders>>> = MutableLiveData()
    private var cancelOrder: MutableLiveData<ApiResponse<ResponseBody>> = MutableLiveData()
    private var orderCart: MutableLiveData<ApiResponse<ResponseBody>> = MutableLiveData()
    private var orderProduct: MutableLiveData<ApiResponse<ResponseBody>> = MutableLiveData()

    fun getOrders(context: Context): MutableLiveData<ApiResponse<List<MyOrders>>> {
        viewModelScope.launch {
            orders = OrderRepository().getOrders(context)
        }
        return orders
    }

    fun cancelOrder(prodId: Int, context: Context): MutableLiveData<ApiResponse<ResponseBody>> {
        viewModelScope.launch {
            cancelOrder = OrderRepository().cancelOrder(prodId, context)
        }
        return cancelOrder
    }

    fun orderCart(addressId:Int,context: Context): MutableLiveData<ApiResponse<ResponseBody>> {
        viewModelScope.launch {
            orderCart = OrderRepository().orderCart(addressId, context)
        }
        return orderCart
    }

    fun orderProduct(addressId:Int, prodId: Int, quantity:String, context: Context): MutableLiveData<ApiResponse<ResponseBody>> {
        viewModelScope.launch {
            orderProduct =
                OrderRepository().orderProduct(addressId, prodId, quantity, context)
        }
        return orderProduct
    }

}
