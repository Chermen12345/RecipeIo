package com.example.recipeio.model

import android.net.Uri

data class User (
    val uid: String = "",
    val username: String= "",
    val email: String="",
    val password: String="",
    val repeatPassword: String="",
    val uri: String=""
        )