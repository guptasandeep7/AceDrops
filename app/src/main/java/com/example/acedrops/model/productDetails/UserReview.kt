package com.example.acedrops.model.productDetails

data class UserReview(
    val createdAt: String,
    val id: Int,
    val productId: Int,
    val rating: Int,
    val review: String,
    val updatedAt: String,
    val userId: Int
)