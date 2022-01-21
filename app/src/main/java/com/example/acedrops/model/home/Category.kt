package com.example.acedrops.model.home

data class Category(
    val category: String,
    val createdAt: String,
    val id: Int,
    val products: List<Product>,
    val updatedAt: String
)