package com.example.recipeio.consts

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseConsts {
    val AUTH = FirebaseAuth.getInstance()
    val REF = Firebase.database.reference
    val STORAGE = Firebase
}