package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepository(private val service: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<ArrayList<Cart>>>()

    fun getCartList(): MutableLiveData<ApiResponse<ArrayList<Cart>>> {
        val call = service.viewCart()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ArrayList<Cart>?> {
                override fun onResponse(
                    call: Call<ArrayList<Cart>?>,
                    response: Response<ArrayList<Cart>?>
                ) {
                    when {
                        response.isSuccessful -> data.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 -> data.postValue(ApiResponse.TokenExpire())
                        response.code() == 402 -> data.postValue(ApiResponse.Error("Invalid Token"))
                        response.code() == 400 -> data.postValue(ApiResponse.Success(null))
                        else -> data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<ArrayList<Cart>?>, t: Throwable) {
                    data.postValue(ApiResponse.Error(t.message))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }

}