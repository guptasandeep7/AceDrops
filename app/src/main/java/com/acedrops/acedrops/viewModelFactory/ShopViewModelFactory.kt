package com.acedrops.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.acedrops.acedrops.repository.home.ShopRepository
import com.acedrops.acedrops.viewmodel.ShopViewModel

class ShopViewModelFactory(private val shopRepository: ShopRepository, private val shopId: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ShopViewModel(shopRepository, shopId) as T

}