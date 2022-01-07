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

    fun getCartList(): ApiResponse<ArrayList<Cart>>? {
        val call = service.viewCart()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ArrayList<Cart>?> {
                override fun onResponse(
                    call: Call<ArrayList<Cart>?>,
                    response: Response<ArrayList<Cart>?>
                ) {
                    when {
                        response.isSuccessful -> data.value = ApiResponse.Success(response.body())
                        response.code() == 400 -> data.value = ApiResponse.TokenExpire()
                        else -> data.value = ApiResponse.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Cart>?>, t: Throwable) {
                    data.value = ApiResponse.Error(t.message)
                }
            })
        } catch (e: Exception) {
            data.value = ApiResponse.Error(e.message)
        }
        return data.value
    }

}