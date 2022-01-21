package com.example.acedrops.repository.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.search.SearchResult
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(val buildService: ApiInterface) {
    private val data = MutableLiveData<ApiResponse<SearchResult>>()

    fun postSearch(text:String): MutableLiveData<ApiResponse<SearchResult>> {
        val call = buildService.postSearch(text)
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<SearchResult?> {
                override fun onResponse(
                    call: Call<SearchResult?>,
                    response: Response<SearchResult?>
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

                override fun onFailure(call: Call<SearchResult?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }
}
