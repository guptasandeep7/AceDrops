package com.example.acedrops.model.productDetails

data class ProductDetails(
    val isFav: Boolean,
    val prod: Prod,
    val category: Category,
    val rating: Float,
    val reviewsAndRatings: List<ReviewsAndRating>,
    val userReview: UserReview
)