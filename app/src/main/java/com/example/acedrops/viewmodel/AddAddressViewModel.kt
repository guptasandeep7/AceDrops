package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.repository.dashboard.AddAddressRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class AddAddressViewModel(val repository: AddAddressRepository) : ViewModel() {

    var houseNo: MutableLiveData<String> = MutableLiveData()
    var street: MutableLiveData<String> = MutableLiveData()
    var locality: MutableLiveData<String> = MutableLiveData()
    var city: MutableLiveData<String> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    private var _result: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()
    val result: LiveData<ApiResponse<Boolean>>
        get() = _result

    fun saveAddress() =
        viewModelScope.launch {
            _result = repository.postAddress(
                AddressResponse(
                    houseNo = houseNo.value.toString(),
                    streetOrPlotNo = street.value.toString(),
                    locality = locality.value.toString(),
                    city = city.value.toString(),
                    state = state.value.toString()
                )
            )
        }

}
