package com.acedrops.acedrops.model.home

data class HomeFragmentData(
    val Shop: List<Shop>,
    val category: List<Category>,
    val newArrival: List<NewArrival>,
    val favProd: List<ProductId>
)