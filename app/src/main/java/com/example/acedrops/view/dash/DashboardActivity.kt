package com.example.acedrops.view.dash

import android.app.AlertDialog.Builder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.acedrops.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

//        bottomNav = findViewById(R.id.bottomNavigationView)
//        bottomNav.setupWithNavController(findNavController(R.id.container2))
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
                finish()
            }
            .setNeutralButton("Cancel") { dialog, id -> }
        val exit = builder.create()
        exit.show()
    }
}