package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.AddAddressRepository
import com.example.acedrops.repository.dashboard.AddressRepository
import com.example.acedrops.repository.dashboard.home.HomeRepository

class AddAddressViewModelFactory(private val addAddressRepository: AddAddressRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AddAddressRepository::class.java).newInstance(addAddressRepository)
    }
}