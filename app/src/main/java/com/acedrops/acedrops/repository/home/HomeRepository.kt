package com.acedrops.acedrops.repository.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.acedrops.acedrops.model.home.HomeFragmentData
import com.acedrops.acedrops.network.ServiceBuilder
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.repository.Datastore.Companion.REF_TOKEN_KEY
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.utill.generateToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback


class HomeRepository {

    private val data = MutableLiveData<ApiResponse<HomeFragmentData>>()

    suspend fun getData(context: Context): MutableLiveData<ApiResponse<HomeFragmentData>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)
        Log.w("HOME REPO", "getData: $token")
        val call = ServiceBuilder.buildService(token).getHome()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<HomeFragmentData?> {
                override fun onResponse(
                    call: Call<HomeFragmentData?>,
                    response: retrofit2.Response<HomeFragmentData?>
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
                                        REF_TOKEN_KEY
                                    )!!, context
                                )
                                getData(context)
                            }

                        }
                        else -> {
                            data.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<HomeFragmentData?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }
}