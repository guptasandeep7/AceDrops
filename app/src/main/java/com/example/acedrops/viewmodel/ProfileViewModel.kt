package com.example.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.repository.profile.ProfileRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private var phoneNo: MutableLiveData<ApiResponse<String>> = MutableLiveData()

    fun getPhoneNo(context: Context): MutableLiveData<ApiResponse<String>> {
        viewModelScope.launch {
                phoneNo = ProfileRepository().getPhoneNumber(context)
        }
        return phoneNo
    }

    fun addPhoneNumber(phnNumber: Long, context: Context): MutableLiveData<ApiResponse<String>>? {
        viewModelScope.launch {
            phoneNo = ProfileRepository().addPhoneNumber(phnNumber, context)
        }
        return phoneNo
    }

}