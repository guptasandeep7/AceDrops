package com.example.acedrops.model

import com.example.acedrops.model.home.Product

data class MyOrders(
    val addressId: Int,
    val createdAt: String,
    val id: Int,
    val price: Int,
    val status: String,
    val updatedAt: String,
    val userId: Int,
    val products:List<Product>
)