package com.eoshopping

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        var navController = Navigation.findNavController(this,R.id.host_fragment )
        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}