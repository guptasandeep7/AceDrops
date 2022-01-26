package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.ProductRepository
import com.example.acedrops.viewmodel.ProductViewModel

class ProductViewModelFactory(
    private val productRepository: ProductRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProductViewModel(productRepository) as T

}