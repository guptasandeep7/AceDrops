package com.acedrops.acedrops.repository.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.acedrops.acedrops.model.AddressResponse
import com.acedrops.acedrops.network.ServiceBuilder
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepository {
    private val data = MutableLiveData<ApiResponse<List<AddressResponse>>>()

    suspend fun getAddress(context: Context): MutableLiveData<ApiResponse<List<AddressResponse>>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).getAddress()
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
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                getAddress(context)
                            }
                        }
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
