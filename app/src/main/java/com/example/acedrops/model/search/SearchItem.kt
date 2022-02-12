package com.example.acedrops.model.search

import com.example.acedrops.model.home.Product

data class SearchItem(
    val id:Int,
    val title:String,
    val type:Int,
    val imageUrl:String?=null,
    val product:Product?=null
)
