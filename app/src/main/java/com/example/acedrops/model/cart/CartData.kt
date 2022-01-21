package com.example.acedrops.model.cart

import com.example.acedrops.model.home.ProductId

data class CartData(
    val prodInCart: List<Cart>,
    val favProd: List<ProductId>
)
