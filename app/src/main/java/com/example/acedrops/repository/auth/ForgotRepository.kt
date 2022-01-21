package com.example.acedrops.repository.auth

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Message
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotRepository {

    var message = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()

    fun forgot(email: String) {
        val request = ServiceBuilder.buildService(null)
        val call = request.forgotPass(UserData(email = email))
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                when {
                    response.isSuccessful -> message.postValue(response.body()?.message)
                    response.code() == 422 -> errorMessage.postValue("Enter correct email id")
                    response.code() == 404 -> errorMessage.postValue("Email id is not registered")
                    else -> errorMessage.postValue("Incorrect Email Id")
                }
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}