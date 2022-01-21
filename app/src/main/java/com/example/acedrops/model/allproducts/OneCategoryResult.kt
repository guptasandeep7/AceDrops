package com.example.acedrops.model.allproducts

import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.ProductId
import java.io.Serializable

data class OneCategoryResult(
    val favProd: List<ProductId>,
    val result: Category?=null
):Serializable