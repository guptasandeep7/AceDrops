package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.AddressRepository
import com.example.acedrops.repository.dashboard.home.HomeRepository

class AddressViewModelFactory(private val addressRepository: AddressRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AddressRepository::class.java).newInstance(addressRepository)
    }
}