package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.home.ProductsRepository

class ProductsViewModelFactory(private val productsRepository: ProductsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductsRepository::class.java)
            .newInstance(productsRepository)
    }
}