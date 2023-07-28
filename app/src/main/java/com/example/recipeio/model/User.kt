package com.example.recipeio.model

import android.net.Uri

data class User (
    val email: String="",
    val password: String="",
    val repeatPassword: String="",
    val uri: String
        )