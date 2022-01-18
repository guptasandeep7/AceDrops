package com.example.acedrops.network

import com.example.acedrops.model.AccessTkn
import com.example.acedrops.model.Message
import com.example.acedrops.model.Token
import com.example.acedrops.model.UserData
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.model.home.HomeFragmentData
import com.example.acedrops.model.home.Product
import retrofit2.Call
import retrofit2.http.*

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
    fun logOut(@Field("refreshToken") refreshToken: String): Call<Message>

    @POST("/auth/signupGoogle")
    fun gSignUp(@Body token: Token): Call<UserData>

    @GET("/prod/home")
    fun getHome(): Call<HomeFragmentData>

    @FormUrlEncoded
    @POST("/prod/addToCart")
    fun addToCart(@Field("prodId") productId: String): Call<CartResponse>

    @FormUrlEncoded
    @POST("/prod/removeFromCart")
    fun removeFromCart(@Field("prodId") productId: String): Call<CartResponse>

    @FormUrlEncoded
    @POST("/prod/deleteCartProd")
    fun deleteFromCart(@Field("prodId") productId: String): Call<CartResponse>

    @FormUrlEncoded
    @POST("/prod/addAndRemFav")
    fun addToWishlist(@Field("prodId") productId: String): Call<WishlistResponse>

    @GET("/prod/viewCart")
    fun viewCart(): Call<CartData>

    @FormUrlEncoded
    @POST("/auth/generateToken")
    fun generateToken(@Field("refreshtoken") refreshToken: String): Call<AccessTkn>

    @GET("/prod/category/{category}")
    fun getProductList(@Path("category") categoryName: String): Call<OneCategoryResult>

    @GET("/prod/viewWishlist")
    fun getWishlist(): Call<List<Product>>

}