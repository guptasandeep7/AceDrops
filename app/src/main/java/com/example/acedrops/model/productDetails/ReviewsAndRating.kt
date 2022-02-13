package com.example.acedrops.model.productDetails

data class ReviewsAndRating(
    val createdAt: String? = null,
    val id: Int? = null,
    val productId: Int? = null,
    val rating: Int,
    val review: String,
    val updatedAt: String? = null,
    val userId: String
)