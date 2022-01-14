package com.example.acedrops.model.home

import java.io.Serializable

data class Shop(
    val description: String,
    val id: Int,
    val imgUrls: List<ImgUrl>,
    val shopName: String
) : Serializable