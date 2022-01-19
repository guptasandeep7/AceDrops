package com.example.acedrops.model

data class AddressResponse(
    val city: String,
    val createdAt: String,
    val houseNo: String,
    val id: Int,
    val locality: String,
    val state: String,
    val streetOrPlotNo: String,
    val updatedAt: String,
    val userId: Int
)