package com.example.acedrops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.acedrops.R

class AuthViewModel : ViewModel() {
    var emailOrPhone: String? = null
    var pass: String? = null
    var newName:String?=null
    var newEmail:String?=null
    var newPhnNo:String?=null
    var otp:String?=null
    var oldPass:String?=null
    var newPass:String?=null


}