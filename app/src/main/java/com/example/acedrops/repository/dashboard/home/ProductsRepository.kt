package com.example.acedrops.repository.dashboard.home

import com.example.acedrops.model.Message
import com.example.acedrops.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsRepository(private val service: ApiInterface) {
    var result: Boolean = false

    fun addToCart(productId: String): Boolean {
        val call = service.addToCart(productId)
        try {
            call.enqueue(object : Callback<Message?> {
                override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                    result = response.isSuccessful
                }

                override fun onFailure(call: Call<Message?>, t: Throwable) {
                    result = false
                }
            })
        } catch (e: Exception) {
            result = false
        }
        return result
    }
}