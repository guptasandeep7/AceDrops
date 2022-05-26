package com.acedrops.acedrops.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.acedrops.acedrops.model.allproducts.OneCategoryResult
import com.acedrops.acedrops.model.cart.CartResponse
import com.acedrops.acedrops.model.cart.WishlistResponse
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.model.productDetails.ProductDetails
import com.acedrops.acedrops.network.ServiceBuilder
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    private val addToCartResult = MutableLiveData<ApiResponse<Boolean>>()
    private val productDetails = MutableLiveData<ApiResponse<ProductDetails>>()
    private val productsList = MutableLiveData<ApiResponse<OneCategoryResult>>()
    private val wishlist = MutableLiveData<ApiResponse<List<Product>>>()
    private val addToWishlist = MutableLiveData<ApiResponse<WishlistResponse>>()
    private val result = MutableLiveData<ApiResponse<ResponseBody>>()

    suspend fun addToCart(productId: Int, context: Context): MutableLiveData<ApiResponse<Boolean>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)
        Log.w("PRODUCT REPO", "addToCart: $token")
        val call = ServiceBuilder.buildService(token).addToCart(productId.toString())
        addToCartResult.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<CartResponse?> {
                override fun onResponse(
                    call: Call<CartResponse?>,
                    response: Response<CartResponse?>
                ) {
                    when {
                        response.isSuccessful -> addToCartResult.postValue(ApiResponse.Success(data = true))
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
                        else -> addToCartResult.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                    addToCartResult.postValue(ApiResponse.Error(t.message))
                }
            })
        } catch (e: Exception) {
            addToCartResult.postValue(ApiResponse.Error(e.message))
        }
        return addToCartResult
    }

    suspend fun addRemoveWishlist(
        productId: String,
        context: Context
    ): MutableLiveData<ApiResponse<WishlistResponse>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).addToWishlist(productId)
        addToWishlist.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<WishlistResponse?> {
                override fun onResponse(
                    call: Call<WishlistResponse?>,
                    response: Response<WishlistResponse?>
                ) {
                    when {
                        response.isSuccessful -> addToWishlist.postValue(
                            ApiResponse.Success(
                                response.body()
                            )
                        )
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                addRemoveWishlist(productId, context)
                            }
                        }
                        else -> addToWishlist.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<WishlistResponse?>, t: Throwable) {
                    addToWishlist.postValue(ApiResponse.Error("Failed : $t"))
                }
            })
        } catch (e: Exception) {
            addToWishlist.postValue(ApiResponse.Error("Error:${e.message}"))
        }
        return addToWishlist
    }

    suspend fun getProductList(
        categoryName: String,
        context: Context
    ): MutableLiveData<ApiResponse<OneCategoryResult>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).getProductList(categoryName)
        productsList.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<OneCategoryResult?> {
                override fun onResponse(
                    call: Call<OneCategoryResult?>,
                    response: Response<OneCategoryResult?>
                ) {
                    when {
                        response.isSuccessful ->
                            productsList.postValue(ApiResponse.Success(response.body()))
                        else ->
                            productsList.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<OneCategoryResult?>, t: Throwable) {
                    productsList.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            productsList.postValue(ApiResponse.Error(e.message))
        }
        return productsList
    }


    suspend fun getProductDetails(
        productId: Int,
        context: Context
    ): MutableLiveData<ApiResponse<ProductDetails>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).getProductDetails(productId)
        productDetails.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ProductDetails?> {
                override fun onResponse(
                    call: Call<ProductDetails?>,
                    response: Response<ProductDetails?>
                ) {
                    when {
                        response.isSuccessful -> productDetails.postValue(
                            ApiResponse.Success(
                                response.body()
                            )
                        )
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                getProductDetails(productId, context)
                            }
                        }
                        else -> productDetails.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<ProductDetails?>, t: Throwable) {
                    productDetails.postValue(ApiResponse.Error(t.message))
                }
            })
        } catch (e: Exception) {
            productDetails.postValue(ApiResponse.Error(e.message))
        }
        return productDetails
    }

    suspend fun getWishlist(context: Context): MutableLiveData<ApiResponse<List<Product>>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).getWishlist()
        wishlist.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<List<Product>?> {
                override fun onResponse(
                    call: Call<List<Product>?>,
                    response: Response<List<Product>?>
                ) {
                    when {
                        response.isSuccessful ->
                            wishlist.postValue(ApiResponse.Success(response.body()))
                        else ->
                            wishlist.postValue(ApiResponse.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                    wishlist.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            wishlist.postValue(ApiResponse.Error(e.message))
        }
        return wishlist
    }

    suspend fun postReviewAndRating(
        prodId: Int,
        review: String,
        rating: String,
        context: Context
    ): MutableLiveData<ApiResponse<ResponseBody>> {
        Log.w("Product REPO", "postReviewAndRating: $prodId,$review,$rating", )

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).postReviewAndRating(prodId, review, rating)
        result.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    when {
                        response.isSuccessful ->
                            result.postValue(ApiResponse.Success(response.body()))
                        else ->
                            result.postValue(ApiResponse.Error(response.code().toString()))
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    result.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            result.postValue(ApiResponse.Error(e.message))
        }
        return result
    }
}
