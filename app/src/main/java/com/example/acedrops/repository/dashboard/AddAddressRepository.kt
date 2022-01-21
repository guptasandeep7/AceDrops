package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.model.Message
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressRepository(val buildService: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<Boolean>>()

    fun postAddress(address:AddressResponse): MutableLiveData<ApiResponse<Boolean>> {
        val call = buildService.postAddress(address = address)
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    when {
                        response.isSuccessful -> data.postValue(ApiResponse.Success(true))
                        response.code() == 403 -> data.postValue(ApiResponse.TokenExpire())
                        response.code() == 402 -> data.postValue(ApiResponse.TokenExpire())
                        else -> data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }
}
