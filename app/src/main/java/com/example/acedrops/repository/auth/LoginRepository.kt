package com.example.acedrops.repository.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    var userDetails = MutableLiveData<UserData>()
    var errorMessage = MutableLiveData<String>()

    fun login(email: String, pass: String) {
        val request = ServiceBuilder.buildService()
        val call = request.login(UserData(email = email, password = pass))
        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                when {
                    response.isSuccessful -> {
                        Log.d("RESPONSE BODY", response.body().toString())
                        userDetails.value = response.body()
                    }
                    response.code() == 401 -> errorMessage.postValue("Wrong password")
                    response.code() == 422 -> errorMessage.postValue("Enter correct email and password")
                    response.code() == 404 -> errorMessage.postValue("User does not exists please signup")
                    else -> errorMessage.postValue("User not registered")
                }
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}