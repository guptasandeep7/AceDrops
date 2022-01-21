package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepository(val buildService: ApiInterface) {
    private val data = MutableLiveData<ApiResponse<List<AddressResponse>>>()

    fun getAddress(): MutableLiveData<ApiResponse<List<AddressResponse>>> {
        val call = buildService.getAddress()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<List<AddressResponse>?> {
                override fun onResponse(
                    call: Call<List<AddressResponse>?>,
                    response: Response<List<AddressResponse>?>
                ) {
                    when {
                        response.isSuccessful -> {
                            data.postValue(ApiResponse.Success(response.body()))
                        }
                        response.code() == 403 -> data.postValue(ApiResponse.TokenExpire())
                        response.code() == 402 -> data.postValue(ApiResponse.TokenExpire())
                        else -> {
                            data.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<List<AddressResponse>?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }
}
