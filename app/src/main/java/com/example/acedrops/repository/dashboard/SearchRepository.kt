package com.example.acedrops.repository.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.search.SearchResult
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository {
    private val data = MutableLiveData<ApiResponse<SearchResult>>()

    suspend fun postSearch(
        text: String,
        context: Context
    ): MutableLiveData<ApiResponse<SearchResult>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).postSearch(text)
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
                        response.code() == 403||response.code()==402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                postSearch(text,context)
                            }
                        }
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
