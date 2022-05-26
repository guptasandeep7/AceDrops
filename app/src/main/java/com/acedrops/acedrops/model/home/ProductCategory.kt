package com.acedrops.acedrops.model.home

import java.io.Serializable

data class ProductCategory(
    val categoryId: Int,
    val createdAt: String,
    val id: Int,
    val productId: Int,
    val updatedAt: String
):Serializable