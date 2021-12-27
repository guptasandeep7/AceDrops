package com.example.acedrops.repository.auth

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Message
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordRepository {

    var message = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()

    fun newPass(email: String,pass:String) {
        val request = ServiceBuilder.buildService()
        val call = request.newPass(UserData(email = email, newpass = pass))
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                if (response.isSuccessful) message.postValue(response.body()?.message)
                else errorMessage.postValue(response.body()?.message?:"Something went wrong! Try again")
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}