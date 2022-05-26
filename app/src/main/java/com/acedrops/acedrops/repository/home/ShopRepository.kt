package com.acedrops.acedrops.repository.home

import androidx.lifecycle.MutableLiveData
import com.acedrops.acedrops.model.home.ShopResult
import com.acedrops.acedrops.network.ApiInterface
import com.acedrops.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopRepository(private val service: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<ShopResult>>()

    fun getShopDetails(shopId: Int): MutableLiveData<ApiResponse<ShopResult>> {
        val call = service.getShopDetails(shopId = shopId)
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ShopResult?> {
                override fun onResponse(call: Call<ShopResult?>, response: Response<ShopResult?>) {
                    when {
                        response.isSuccessful ->
                            data.postValue(ApiResponse.Success(response.body()))
                        else ->
                            data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<ShopResult?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }

}
