package com.acedrops.acedrops.repository.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.acedrops.acedrops.model.MyOrders
import com.acedrops.acedrops.network.ServiceBuilder
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.utill.generateToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {

    private val data = MutableLiveData<ApiResponse<List<MyOrders>>>()
    private val cancelOrder = MutableLiveData<ApiResponse<ResponseBody>>()
    private val orderCart = MutableLiveData<ApiResponse<ResponseBody>>()
    private val orderProduct = MutableLiveData<ApiResponse<ResponseBody>>()

    suspend fun getOrders(context: Context): MutableLiveData<ApiResponse<List<MyOrders>>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).getOrders()
        data.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<List<MyOrders>?> {
                override fun onResponse(
                    call: Call<List<MyOrders>?>,
                    response: Response<List<MyOrders>?>
                ) {
                    when {
                        response.isSuccessful -> {
                            data.postValue(ApiResponse.Success(response.body()))
                        }
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                getOrders(context)
                            }
                        }
                        else -> {
                            data.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<List<MyOrders>?>, t: Throwable) {
                    data.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            data.postValue(ApiResponse.Error(e.message))
        }
        return data
    }


    suspend fun cancelOrder(
        prodId: Int,
        context: Context
    ): MutableLiveData<ApiResponse<ResponseBody>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).cancelOrder(prodId)
        cancelOrder.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    when {
                        response.isSuccessful -> {
                            cancelOrder.postValue(ApiResponse.Success(response.body()))
                        }
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                cancelOrder(prodId, context)
                            }
                        }
                        else -> {
                            cancelOrder.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    cancelOrder.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            cancelOrder.postValue(ApiResponse.Error(e.message))
        }
        return cancelOrder
    }


    suspend fun orderCart(
        addressId: Int,
        context: Context
    ): MutableLiveData<ApiResponse<ResponseBody>> {

        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token).orderCart(addressId.toString())
        orderCart.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    when {
                        response.isSuccessful -> {
                            orderCart.postValue(ApiResponse.Success(response.body()))
                        }
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                orderCart(addressId, context)
                            }
                        }
                        else -> {
                            orderCart.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    orderCart.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            orderCart.postValue(ApiResponse.Error(e.message))
        }
        return orderCart
    }


    suspend fun orderProduct(
        addressId: Int,
        prodId: Int,
        quantity: String,
        context: Context
    ): MutableLiveData<ApiResponse<ResponseBody>> {

        Log.w("ORDER REPO", "orderProduct: $addressId, $prodId, $quantity", )
        val token = Datastore(context).getUserDetails(Datastore.ACCESS_TOKEN_KEY)

        val call = ServiceBuilder.buildService(token)
            .orderProduct(addressId.toString(), prodId.toString(), quantity)
        orderProduct.postValue(ApiResponse.Loading())
        try {
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    when {
                        response.isSuccessful -> {
                            orderProduct.postValue(ApiResponse.Success(response.body()))
                        }
                        response.code() == 403 || response.code() == 402 -> {
                            GlobalScope.launch {
                                generateToken(
                                    token!!,
                                    Datastore(context).getUserDetails(
                                        Datastore.REF_TOKEN_KEY
                                    )!!, context
                                )
                                orderProduct(addressId, prodId, quantity, context)
                            }
                        }
                        else -> {
                            orderProduct.postValue(ApiResponse.Error(response.message()))
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    orderProduct.postValue(ApiResponse.Error("Something went wrong!! ${t.message}"))
                }
            })
        } catch (e: Exception) {
            orderProduct.postValue(ApiResponse.Error(e.message))
        }
        return orderProduct
    }
}