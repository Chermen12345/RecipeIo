package com.example.recipeio.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeio.R
import com.example.recipeio.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }
    private fun setUpNavigation(){
        binding.apply {
            val navigation = findNavController(R.id.navhost)
            btnav.setupWithNavController(navigation)
        }
    }
}