package com.acedrops.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.AddressResponse
import com.acedrops.acedrops.repository.profile.AddressRepository
import com.acedrops.acedrops.utill.ApiResponse
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
