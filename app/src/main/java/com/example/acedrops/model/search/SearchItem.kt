package com.example.acedrops.model.search

data class SearchItem(
    val id:Int,
    val title:String,
    val type:Int,
    val imageUrl:String?=null
)
