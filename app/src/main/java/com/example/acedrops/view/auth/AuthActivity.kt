package com.example.acedrops.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class AuthActivity : AppCompatActivity() {
    companion object {
        var ACC_TOKEN: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.acedrops.R.layout.activity_auth)
        supportActionBar?.hide()
    }
}