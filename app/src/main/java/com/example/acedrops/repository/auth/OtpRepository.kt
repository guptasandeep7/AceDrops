package com.example.acedrops.repository.auth

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Message
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepository {

    var userData = MutableLiveData<UserData>()
    var errorMessage = MutableLiveData<String>()
    var message = MutableLiveData<String>()

    fun otp(email: String, pass: String, name: String, otp: String) {
        val request = ServiceBuilder.buildService()
        val call = request.signUpVerify(
            UserData(
                email = email,
                password = pass,
                name = name,
                otp = otp,
                isShop = false
            )
        )
        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.isSuccessful) userData.postValue(response.body())
                else errorMessage.postValue(response.body()?.message?: "Incorrect OTP")
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun forgotOtp(email: String,otp: String){
        val request = ServiceBuilder.buildService()
        val call = request.forgotVerify(UserData(email = email, otp = otp))
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                if(response.isSuccessful) message.postValue(response.body()?.message)
                else errorMessage.postValue(response.body()?.message?:"Incorrect OTP")
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}