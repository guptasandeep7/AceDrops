package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.home.HomeRepository

class HomeViewModelFactory(val homeRepository: HomeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeRepository::class.java).newInstance(homeRepository)
    }
}