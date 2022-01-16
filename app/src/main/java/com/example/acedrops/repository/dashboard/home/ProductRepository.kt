package com.example.acedrops.repository.dashboard.home

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.network.ApiInterface
import com.example.acedrops.utill.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val service: ApiInterface) {

    private val data = MutableLiveData<ApiResponse<OneCategoryResult>>()

    fun getProductList(categoryName: String): MutableLiveData<ApiResponse<OneCategoryResult>> {
        val call = service.getProductList(categoryName)
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<OneCategoryResult?> {
                override fun onResponse(
                    call: Call<OneCategoryResult?>,
                    response: Response<OneCategoryResult?>
                ) {
                    when {
                        response.isSuccessful ->
                            data.postValue(ApiResponse.Success(response.body()))
                        else ->
                            data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<OneCategoryResult?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }
}
