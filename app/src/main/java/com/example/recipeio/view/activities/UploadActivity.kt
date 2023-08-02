package com.example.recipeio.view.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.recipeio.R
import com.example.recipeio.databinding.ActivityUploadBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.model.User
import com.example.recipeio.presenter.UploadPresenterImpl
import com.example.recipeio.presenter.UploadView
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.Consts.STORAGE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadActivity : AppCompatActivity(), UploadView {
    private lateinit var binding: ActivityUploadBinding
    //TODO presenter class with functions for database
    private val presenter = UploadPresenterImpl()
    //TODO list of our images user choose from gallery
    private var image: Uri?=null

    lateinit var username :String
    lateinit var imageprofile : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        presenter.attach(this)

        //db
        uploadRecipe()
        getImages()

        getUser()
    }




    //TODO db

    //upload to firebase real time data base the recipe
    private fun uploadRecipe(){
        binding.apply {
            btUpload.setOnClickListener {
                val name = edfoodname.text.toString()
                val desc = eddescription.text.toString()
                val ingr = edingredients.text.toString()
                val uid = AUTH.currentUser!!.uid

                if (image!=null){
                    val recipe = Recipe(desc, name,image.toString(),ingr,uid ,imageprofile,username)
                    lifecycleScope.launch(Dispatchers.Main){
                        presenter.insertRecipe(recipe)
                    }
                }else{
                    message("choose image")
                }


            }
        }
    }

    //TODO navigation
    //close the activity and go back to home activity

    override fun close() {
        finish()
    }

    //TODO utils

    //message which shows when for example user didnt filled the whole plains
    override fun message(string: String) {
        Toast.makeText(this,string,Toast.LENGTH_LONG).show()
    }
    //picking images from gallery
    private fun getImages(){
        binding.img.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)
        }

    }
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        result.data?.data?.let {
            binding.img.setImageURI(it)
            image = it
        }

    }

    private fun getUser(){
        REF.child("users/${AUTH.currentUser!!.uid}").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(User::class.java)
                value?.let {
                    username = it.username
                    imageprofile = it.uri
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}