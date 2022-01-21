package com.example.acedrops.model.cart

data class CartItem(
    val cartId: Int,
    val createdAt: String,
    val id: Int,
    val productId: Int,
    var quantity: Int,
    val updatedAt: String
)