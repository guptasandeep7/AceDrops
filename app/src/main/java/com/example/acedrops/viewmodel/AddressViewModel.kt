package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.repository.dashboard.AddressRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class AddressViewModel(val repository: AddressRepository) : ViewModel() {

    private var _address: MutableLiveData<ApiResponse<List<AddressResponse>>> = MutableLiveData()
    val address: LiveData<ApiResponse<List<AddressResponse>>>
        get() = _address

    fun getAddress() = viewModelScope.launch {
        _address = repository.getAddress()
    }

    init {
        getAddress()
    }

}
