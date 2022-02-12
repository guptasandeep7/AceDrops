package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.home.ShopRepository
import com.example.acedrops.viewmodel.ShopViewModel

class ShopViewModelFactory(private val shopRepository: ShopRepository, private val shopId: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ShopViewModel(shopRepository, shopId) as T

}