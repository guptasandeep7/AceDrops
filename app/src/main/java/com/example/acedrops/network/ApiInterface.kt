package com.example.acedrops.network

import com.example.acedrops.model.Message
import com.example.acedrops.model.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @POST("/auth/signup")
    fun signup(@Body data: UserData): Call<Message>

    @POST("/auth/login")
    fun login(@Body data: UserData): Call<UserData>

    @POST("/auth/signup/verify")
    fun signUpVerify(@Body data: UserData): Call<UserData>

    @POST("/auth/forgotPass")
    fun forgotPass(@Body email: UserData): Call<Message>

    @POST("/auth/forgotPassVerify")
    fun forgotVerify(@Body data: UserData): Call<Message>

    @POST("/auth/newpass")
    fun newPass(@Body data: UserData): Call<Message>

    @FormUrlEncoded
    @POST("/auth/logout")
    fun logOut(@Field("refreshToken") refreshToken:String): Call<Message>

}