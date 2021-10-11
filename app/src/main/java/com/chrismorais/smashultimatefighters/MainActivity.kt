package com.chrismorais.smashultimatefighters

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SmashUltimateFighters)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupNavigation()

        firstAccess()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun firstAccess() {
        if (getPreferences(Context.MODE_PRIVATE).getBoolean("SHOWED_ONBOARD", false)) {
            navController.popBackStack()
            navController.navigate(R.id.action_global_fightersListFragment)
        }
    }
}