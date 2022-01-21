package com.example.acedrops.model.home

data class NewArrival(
    val basePrice: Int,
    val createdAt: String,
    val description: String,
    val discountedPrice: Int,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val offers: String,
    val shopId: Int,
    val stock: Int,
    val title: String,
    val updatedAt: String
)