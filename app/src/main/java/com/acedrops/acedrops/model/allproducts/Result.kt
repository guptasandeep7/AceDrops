package com.acedrops.acedrops.model.allproducts

import com.acedrops.acedrops.model.home.Product

data class Result(
    val category: String,
    val createdAt: String,
    val id: Int,
    val products: List<Product>,
    val updatedAt: String
)