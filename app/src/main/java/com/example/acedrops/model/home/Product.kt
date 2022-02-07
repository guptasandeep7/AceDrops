package com.example.acedrops.model.home

import java.io.Serializable

data class Product(
    val basePrice: Int,
    val createdAt: String,
    val shortDescription: String,
    val discountedPrice: Int,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val offers: String,
    val product_category: ProductCategory?=null,
    val shopId: Int,
    val stock: Int,
    val title: String,
    val updatedAt: String,
    var wishlistStatus: Int = 0
) : Serializable