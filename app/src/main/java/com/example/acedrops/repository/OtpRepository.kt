package com.example.acedrops.repository

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.UserData
import com.example.acedrops.model.Message
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepository(){

    var message = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()

    fun otp(email:String, pass:String, name:String,otp:String){
        val request = ServiceBuilder.buildService()
        val call = request.signUpVerify(UserData(email = email, password = pass, name = name, otp = otp, isShop = false))
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                if(response.isSuccessful) message.value = response.body()?.message
                else errorMessage.value = response.body()?.message
            }
            override fun onFailure(call: Call<Message?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}