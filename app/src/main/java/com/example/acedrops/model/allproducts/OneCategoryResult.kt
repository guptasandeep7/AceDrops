package com.example.acedrops.model.allproducts

import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.productId
import java.io.Serializable

data class OneCategoryResult(
    val favProd: List<productId>,
    val result: Category?=null
):Serializable