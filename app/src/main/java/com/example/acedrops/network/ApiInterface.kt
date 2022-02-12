package com.example.acedrops.network

import com.example.acedrops.model.*
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.model.home.HomeFragmentData
import com.example.acedrops.model.home.Product
import com.example.acedrops.model.home.ShopResult
import com.example.acedrops.model.productDetails.ProductDetails
import com.example.acedrops.model.search.SearchResult
import okhttp3.ResponseBody
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
    @POST("/auth/changePass")
    fun changePass(
        @Field("email") email: String,
        @Field("password") oldPass: String,
        @Field("newpass") newPass: String
    ): Call<Message>

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

    @GET("/user/getAddress")
    fun getAddress(): Call<List<AddressResponse>>

    @POST("/user/addAddress")
    fun postAddress(@Body address: AddressResponse): Call<String>

    @GET("/shop/viewOneShop/{shopId}")
    fun getShopDetails(@Path("shopId") shopId: Int): Call<ShopResult>

    @GET("/prod/viewProd/{prodId}")
    fun getProductDetails(@Path("prodId") prodId: Int): Call<ProductDetails>

    @FormUrlEncoded
    @POST("/user/search")
    fun postSearch(@Field("toSearch") toSearch: String): Call<SearchResult>

    @GET("/user/getOrders")
    fun getOrders(): Call<List<MyOrders>>

    @POST("/user/cancelOrder/{orderId}")
    fun cancelOrder(@Path("orderId") orderId: Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/user/orderCart")
    fun orderCart(@Field("addressId") addressId: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/user/orderProd")
    fun orderProduct(
        @Field("addressId") addressId: String,
        @Field("prodId") prodId: String,
        @Field("quantity") quantity: String
    ): Call<ResponseBody>

    @GET("/user/getPhno")
    fun getPhoneNumber(): Call<String>

    @FormUrlEncoded
    @POST("/user/addPhno")
    fun addPhoneNumber(@Field("phno") phnNo: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/user/reviews/{prodId}")
    fun postReviewAndRating(
        @Path("prodId") prodId: Int,
        @Field("review") review: String,
        @Field("rating") rating: String
    ): Call<ResponseBody>
}