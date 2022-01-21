package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.CartRepository

class CartViewModelFactory(private val cartRepository: CartRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CartRepository::class.java).newInstance(cartRepository)
    }
}