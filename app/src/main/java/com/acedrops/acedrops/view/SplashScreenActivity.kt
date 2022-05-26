package com.acedrops.acedrops.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.repository.Datastore.Companion.PHN_NUMBER
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.view.auth.AuthActivity
import com.acedrops.acedrops.view.dash.DashboardActivity
import com.acedrops.acedrops.viewmodel.HomeViewModel
import com.acedrops.acedrops.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var datastore: Datastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datastore = Datastore(this)

        lifecycleScope.launch {
            if (datastore.isLogin()) {
                getPhnNumber()
                homeViewModel.getHomeData(this@SplashScreenActivity)
                startActivity(Intent(this@SplashScreenActivity, DashboardActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreenActivity, AuthActivity::class.java))
                finish()
            }

        }
    }

    private fun getPhnNumber() {
        profileViewModel.getPhoneNo(this).observe(this) {
            lifecycleScope.launch {
                if (it is ApiResponse.Success) datastore.saveUserDetails(PHN_NUMBER, it.data)
            }
        }
    }

}