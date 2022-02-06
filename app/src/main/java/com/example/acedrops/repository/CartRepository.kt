package com.example.acedrops.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepository {

    private val data = MutableLiveData<ApiResponse<CartData>>()
    private val atcResult = MutableLiveData<ApiResponse<CartResponse>>()
    private val removeFromCart = MutableLiveData<ApiResponse<CartResponse>>()
    private val deleteFromCart = MutableLiveData<ApiResponse<CartResponse>>()
    private val wishlist = MutableLiveData<ApiResponse<WishlistResponse>>()

    suspend fun getCartList(context: Context): MutableLiveData<ApiResponse<CartData>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).viewCart()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartData?> {
                override fun onResponse(call: Call<CartData?>, response: Response<CartData?>) {
                    when {
                        response.isSuccessful -> data.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 || response.code() == 402 -> {

                            runBlocking {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                getCartList(context)
                            }

                        }
                        response.code() == 400 -> data.postValue(ApiResponse.Success(null))
                        else -> data.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartData?>, t: Throwable) {
                    data.postValue(ApiResponse.Error(t.message))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }

    suspend fun addToCart(
        productId: String,
        context: Context
    ): MutableLiveData<ApiResponse<CartResponse>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).addToCart(productId)
        atcResult.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartResponse?> {
                override fun onResponse(
                    call: Call<CartResponse?>,
                    response: Response<CartResponse?>
                ) {
                    when {
                        response.isSuccessful -> atcResult.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                addToCart(productId, context)
                            }
                        }
                        else -> atcResult.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                    atcResult.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            atcResult.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return atcResult
    }

    suspend fun removeFromCart(
        productId: String,
        context: Context
    ): MutableLiveData<ApiResponse<CartResponse>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).removeFromCart(productId)
        removeFromCart.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartResponse?> {
                override fun onResponse(
                    call: Call<CartResponse?>,
                    response: Response<CartResponse?>
                ) {
                    when {
                        response.isSuccessful -> removeFromCart.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                addToCart(productId, context)
                            }
                        }
                        else -> removeFromCart.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                    removeFromCart.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            removeFromCart.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return removeFromCart
    }

    suspend fun deleteFromCart(
        productId: String,
        context: Context
    ): MutableLiveData<ApiResponse<CartResponse>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).deleteFromCart(productId)
        deleteFromCart.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartResponse?> {
                override fun onResponse(
                    call: Call<CartResponse?>,
                    response: Response<CartResponse?>
                ) {
                    when {
                        response.isSuccessful -> deleteFromCart.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                addToCart(productId, context)
                            }
                        }
                        else -> deleteFromCart.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                    deleteFromCart.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            deleteFromCart.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return deleteFromCart
    }

    suspend fun addRemoveWishlist(productId: String,context: Context): MutableLiveData<ApiResponse<WishlistResponse>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).addToWishlist(productId)
        wishlist.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<WishlistResponse?> {
                override fun onResponse(
                    call: Call<WishlistResponse?>,
                    response: Response<WishlistResponse?>
                ) {
                    when {
                        response.isSuccessful -> wishlist.postValue(ApiResponse.Success(response.body()))
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                addToCart(productId, context)
                            }
                        }
                        else -> wishlist.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<WishlistResponse?>, t: Throwable) {
                    wishlist.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            wishlist.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return wishlist
    }
}