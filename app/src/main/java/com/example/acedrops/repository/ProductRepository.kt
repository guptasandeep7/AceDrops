package com.example.acedrops.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.productDetails.ProductDetails
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    private val addToCartResult = MutableLiveData<ApiResponse<Boolean>>()
    private val productDetails = MutableLiveData<ApiResponse<ProductDetails>>()
    private val productsList = MutableLiveData<ApiResponse<OneCategoryResult>>()

    suspend fun addToCart(productId: Int, context: Context): MutableLiveData<ApiResponse<Boolean>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

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


    suspend fun getProductList(categoryName: String, context: Context): MutableLiveData<ApiResponse<OneCategoryResult>> {

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
}
