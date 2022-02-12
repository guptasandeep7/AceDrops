package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.repository.profile.AddressRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {

    private var _address: MutableLiveData<ApiResponse<List<AddressResponse>>> = MutableLiveData()

    fun getAddress(context: Context): MutableLiveData<ApiResponse<List<AddressResponse>>> {
        viewModelScope.launch {
            _address = AddressRepository().getAddress(context)
        }
        return _address
    }

}
