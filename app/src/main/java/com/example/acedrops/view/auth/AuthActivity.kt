package com.example.acedrops.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.acedrops.repository.Datastore
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {
    companion object {
        var ACC_TOKEN: String? = null
    }

    lateinit var datastore: Datastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.acedrops.R.layout.activity_auth)
        supportActionBar?.hide()
        datastore = Datastore(this)

        lifecycleScope.launch {
            token()
        }
    }

    private suspend fun token() {
        ACC_TOKEN = datastore.getUserDetails(Datastore.ACCESS_TOKEN_KEY)
    }
}