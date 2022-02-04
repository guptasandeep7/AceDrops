package com.example.acedrops.model.productDetails

import com.example.acedrops.model.home.ImgUrl

data class Prod(
    val basePrice: Int,
    val createdAt: String,
    val description: String,
    val discountedPrice: Int,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val offers: String,
    val shop: Shop,
    val shopId: Int,
    val stock: Int,
    val title: String,
    val updatedAt: String
)