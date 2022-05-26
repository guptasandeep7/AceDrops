package com.acedrops.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.AddressResponse
import com.acedrops.acedrops.repository.profile.AddAddressRepository
import com.acedrops.acedrops.utill.ApiResponse
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
