package com.example.acedrops.model.home

data class ShopResult(
    val description: String,
    val email: String,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val name: String,
    val phno: String,
    val shopName: String,
    val products: List<Product>,
    val address:String

)