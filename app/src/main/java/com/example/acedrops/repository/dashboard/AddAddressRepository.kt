package com.example.acedrops.repository.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.model.Message
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressRepository {

    private val data = MutableLiveData<ApiResponse<Boolean>>()

    suspend fun postAddress(address:AddressResponse,context: Context): MutableLiveData<ApiResponse<Boolean>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).postAddress(address = address)
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
