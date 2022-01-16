package com.example.acedrops.model.allproducts

import com.example.acedrops.model.home.Product

data class Result(
    val category: String,
    val createdAt: String,
    val id: Int,
    val products: List<Product>,
    val updatedAt: String
)