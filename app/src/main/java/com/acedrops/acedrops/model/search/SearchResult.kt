package com.acedrops.acedrops.model.search

import com.acedrops.acedrops.model.home.Category
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.model.home.ProductId
import com.acedrops.acedrops.model.home.Shop

data class SearchResult(
    val categoryProds: List<Category>? = null,
    val favProd: List<ProductId>? = null,
    val products: List<Product>? = null,
    val shops: List<Shop>? = null
)