package com.example.recipeio.model

import android.net.Uri

data class User (
    val id: String="",
    val email: String="",
    val password: String="",
    var uri: Uri?=null
        )