package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Message
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepository(private val service: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<CartData>>()
    private val atcResult = MutableLiveData<ApiResponse<Boolean>>()
    private val removeFromCart = MutableLiveData<ApiResponse<Boolean>>()
    private val wishlist = MutableLiveData<ApiResponse<Boolean>>()

    fun getCartList(): MutableLiveData<ApiResponse<CartData>> {
        val call = service.viewCart()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartData?> {
                override fun onResponse(call: Call<CartData?>, response: Response<CartData?>) {
                    when {
                        response.isSuccessful -> data.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 -> data.postValue(ApiResponse.TokenExpire())
                        response.code() == 402 -> data.postValue(ApiResponse.Error("Invalid Token"))
                        response.code() == 400 -> data.postValue(ApiResponse.Success(null))
                        else -> data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartData?>, t: Throwable) {
                    data.postValue(ApiResponse.Error(t.message))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }

    fun addToCart(productId: String): MutableLiveData<ApiResponse<Boolean>> {
        val call = service.addToCart(productId)
        atcResult.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<Message?> {
                override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                   if (response.isSuccessful) atcResult.postValue(ApiResponse.Success(true))
                    else atcResult.postValue(ApiResponse.Error(response.message()))
                }

                override fun onFailure(call: Call<Message?>, t: Throwable) {
                    atcResult.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            atcResult.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return atcResult
    }

    fun removeFromCart(productId: String): MutableLiveData<ApiResponse<Boolean>> {
        val call = service.removeFromCart(productId)
        removeFromCart.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<Message?> {
                override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                   if (response.isSuccessful) removeFromCart.postValue(ApiResponse.Success(true))
                    else removeFromCart.postValue(ApiResponse.Error(response.message()))
                }

                override fun onFailure(call: Call<Message?>, t: Throwable) {
                    removeFromCart.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            removeFromCart.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return removeFromCart
    }

    fun addRemoveWishlist(productId: String): MutableLiveData<ApiResponse<Boolean>> {
        val call = service.addToWishlist(productId)
        wishlist.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful) wishlist.postValue(ApiResponse.Success(true))
                    else wishlist.postValue(ApiResponse.Error(response.message()))
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    wishlist.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            wishlist.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return wishlist
    }
}