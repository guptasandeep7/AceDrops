package com.acedrops.acedrops.model.allproducts

import com.acedrops.acedrops.model.home.Category
import com.acedrops.acedrops.model.home.ProductId
import java.io.Serializable

data class OneCategoryResult(
    val favProd: List<ProductId>,
    val result: Category?=null
):Serializable