package com.example.acedrops.model.search

import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.Product
import com.example.acedrops.model.home.ProductId
import com.example.acedrops.model.home.Shop

data class SearchResult(
    val categoryProds: List<Category>? = null,
    val favProd: List<ProductId>? = null,
    val products: List<Product>? = null,
    val shops: List<Shop>? = null
)