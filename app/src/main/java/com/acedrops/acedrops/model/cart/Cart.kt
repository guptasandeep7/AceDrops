package com.acedrops.acedrops.model.cart

import com.acedrops.acedrops.model.home.ImgUrl

data class Cart(
    val basePrice: Int,
    val cart_item: CartItem,
    val createdAt: String,
    val categories: List<ProductCategory>,
    val shortDescription: String,
    val discountedPrice: Int,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val offers: String,
    val shopId: Int,
    val stock: Int,
    val title: String,
    val updatedAt: String,
    var wishlistStatus: Int = 0
)