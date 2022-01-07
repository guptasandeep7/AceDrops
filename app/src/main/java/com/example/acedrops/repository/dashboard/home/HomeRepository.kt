package com.example.acedrops.repository.dashboard.home

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.home.HomeFragmentData
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback


class HomeRepository(private val service: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<HomeFragmentData>>()

    fun getData(): MutableLiveData<ApiResponse<HomeFragmentData>> {
        val call = service.getHome()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<HomeFragmentData?> {
                override fun onResponse(
                    call: Call<HomeFragmentData?>,
                    response: retrofit2.Response<HomeFragmentData?>
                ) {
                    if (response.isSuccessful) {
                        data.postValue(ApiResponse.Success(response.body()))
                    } else {
                        data.postValue(ApiResponse.Error(response.message()))
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