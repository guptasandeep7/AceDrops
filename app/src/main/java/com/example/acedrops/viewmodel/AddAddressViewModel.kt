package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.repository.profile.AddAddressRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class AddAddressViewModel : ViewModel() {

    var houseNo: MutableLiveData<String> = MutableLiveData()
    var street: MutableLiveData<String> = MutableLiveData()
    var locality: MutableLiveData<String> = MutableLiveData()
    var city: MutableLiveData<String> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    private var _result: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()

    fun saveAddress(context: Context): MutableLiveData<ApiResponse<Boolean>> {
        viewModelScope.launch {
            _result = AddAddressRepository().postAddress(
                AddressResponse(
                    houseNo = houseNo.value.toString(),
                    streetOrPlotNo = street.value.toString(),
                    locality = locality.value.toString(),
                    city = city.value.toString(),
                    state = state.value.toString()
                ),
                context
            )

        }
        return _result
    }

}
