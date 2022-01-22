package com.example.acedrops.repository.auth

import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Token
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSignRepository {

    var userData = MutableLiveData<UserData>()
    var errorMessage = MutableLiveData<String>()


}