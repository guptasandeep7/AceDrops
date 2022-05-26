package com.acedrops.acedrops.model.cart

import com.acedrops.acedrops.model.home.ProductId

data class CartData(
    val prodInCart: List<Cart>,
    val favProd: List<ProductId>
)
