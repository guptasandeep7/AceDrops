package com.acedrops.acedrops.model

data class AddressResponse(
    val city: String,
    val createdAt: String? = null,
    val houseNo: String,
    val id: Int? = null,
    val locality: String,
    val state: String,
    val streetOrPlotNo: String,
    val updatedAt: String? = null,
    val userId: Int? = null
)