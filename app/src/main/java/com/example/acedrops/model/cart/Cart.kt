package com.example.acedrops.model.cart

data class Cart(
    val basePrice: Int,
    val cart_item: CartItem,
    val createdAt: String,
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