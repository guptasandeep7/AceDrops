package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.home.ProductRepository

class ProductsViewModelFactory(private val productRepository: ProductRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductRepository::class.java)
            .newInstance(productRepository)
    }
}