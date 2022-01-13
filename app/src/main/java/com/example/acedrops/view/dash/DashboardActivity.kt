package com.example.acedrops.view.dash

import android.app.AlertDialog.Builder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.ActivityDashboardBinding
import com.example.acedrops.repository.Datastore
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    companion object {
        var ACC_TOKEN: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dataStore = Datastore(this)
        lifecycleScope.launch {
            ACC_TOKEN = dataStore.getUserDetails(Datastore.ACCESS_TOKEN_KEY).toString()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container2) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
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