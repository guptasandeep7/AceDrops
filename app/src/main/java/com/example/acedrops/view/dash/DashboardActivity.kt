package com.example.acedrops.view.dash

import android.app.AlertDialog.Builder
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import com.example.acedrops.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onBackPressed() =
        when (findNavController(R.id.container2).currentDestination?.id) {
            R.id.homeFragment -> alertBox()
            else -> super.onBackPressed()
        }

    private fun alertBox() {
        val builder = Builder(this)
        builder.setTitle("Exit")
            .setMessage("Are you sure you want to Exit?")
            .setPositiveButton("Exit") { dialog, id ->
                super.onBackPressed()
            }
            .setNeutralButton("Cancel") { dialog, id -> }
        val exit = builder.create()
        exit.show()
    }
}