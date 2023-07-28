package com.example.recipeio

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object Consts {
    val AUTH = FirebaseAuth.getInstance()
    val REF = Firebase.database.reference
    val STORAGE = FirebaseStorage.getInstance().reference
}