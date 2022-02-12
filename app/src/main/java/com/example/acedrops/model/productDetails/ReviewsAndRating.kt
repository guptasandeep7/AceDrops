package com.example.acedrops.model.productDetails

data class ReviewsAndRating(
    val createdAt: String,
    val id: Int,
    val productId: Int,
    val rating: Int,
    val review: String,
    val updatedAt: String,
    val userId: String
)