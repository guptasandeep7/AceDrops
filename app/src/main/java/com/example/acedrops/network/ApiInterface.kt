package com.example.acedrops.network

import com.example.acedrops.model.UserData
import com.example.acedrops.model.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/signup")
    fun signup(@Body data: UserData): Call<Message>

    @POST("auth/login")
    fun login(@Body data: UserData): Call<UserData>

    @POST("auth/signup/verify")
    fun signUpVerify(@Body data: UserData): Call<Message>

    @POST("auth/forgotPass")
    fun forgotPass(@Body email: UserData): Call<UserData>

    @POST("auth/forgotPassVerify")
    fun verifyPass(@Body data: UserData): Call<UserData>

    @POST("auth/newpass")
    fun newPass(@Body data: UserData): Call<UserData>

}