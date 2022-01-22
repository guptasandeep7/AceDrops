package com.example.acedrops.repository.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    var userDetails = MutableLiveData<UserData>()
    var errorMessage = MutableLiveData<String>()


}