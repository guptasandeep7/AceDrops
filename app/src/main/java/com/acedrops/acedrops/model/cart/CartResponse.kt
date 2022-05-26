package com.acedrops.acedrops.model.cart

data class CartResponse(
    val price: Int,
    val quantity: Int?=null,
    val prodId: Int
)