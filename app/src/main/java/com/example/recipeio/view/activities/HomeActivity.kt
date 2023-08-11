package com.example.recipeio.view.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeio.NotificationService
import com.example.recipeio.R
import com.example.recipeio.databinding.ActivityHomeBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.view.adapters.RecipeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var launcher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
        goToUpLoadActivity()

        registerLauncher()
        lifecycleScope.launch(Dispatchers.Main){

            hasPerms()
        }


    }
    private fun setUpNavigation(){
        binding.apply {
            val navigation = findNavController(R.id.navhost)
            btnav.setupWithNavController(navigation)
        }
    }
    private fun goToUpLoadActivity(){
        binding.fab.setOnClickListener {
            val intent = Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }

    }

    private suspend fun hasPerms(){
        if (ActivityCompat.checkSelfPermission(applicationContext,Manifest.permission.POST_NOTIFICATIONS)
        ==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"permission granted",Toast.LENGTH_LONG).show()
            createNotifChannel()
        }else{
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun registerLauncher(){
        launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"you denied the permission, notifications will not come to your device,"
                        ,Toast.LENGTH_LONG).show()
                Toast.makeText(this, "to accept the permission, go to your settings and accept it",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun createNotifChannel(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    companion object{
        const val CHANNEL_ID = "FOLLOWING_CHANNEL_ID"
        const val CHANNEL_NAME = "FOLLOWING_CHANNEL"
    }



}