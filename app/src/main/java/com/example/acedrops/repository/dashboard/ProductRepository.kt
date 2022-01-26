package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ProductRepository(private val service: ApiInterface) {

    private val addToCartResult = MutableLiveData<ApiResponse<Boolean>>()

    suspend fun addToCart(productId: Int): MutableLiveData<ApiResponse<Boolean>> {
        val call = service.addToCart(productId.toString())
        addToCartResult.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartResponse?> {
                override fun onResponse(
                    call: Call<CartResponse?>,
                    response: Response<CartResponse?>
                ) {
                    if (response.isSuccessful) addToCartResult.postValue(ApiResponse.Success(data = true))
                    else addToCartResult.postValue(ApiResponse.Error(response.message()))
                }

                override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                    addToCartResult.postValue(ApiResponse.Error(t.message))
                }
            })
        }catch (e:Exception){
            addToCartResult.postValue(ApiResponse.Error(e.message))
        }
        return addToCartResult
    }
}
