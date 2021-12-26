package com.example.acedrops.view.dash

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import com.example.acedrops.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

//    override fun onBackPressed() {
//        when(findNavController(R.id.container2).currentDestination?.id){
//            R.id.HomeFragment -> {
//                finish()
//            }
//        }
//        super.onBackPressed()
//    }
}