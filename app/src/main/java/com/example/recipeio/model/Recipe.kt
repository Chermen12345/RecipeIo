package com.example.recipeio.model

import android.net.Uri
import java.io.Serializable

data class Recipe(
    val description: String="",
    val foodName: String="",
    val image: String="",
    val ingredients: String="",
    val ownerId: String="",
    val userImage: String="",
    val username: String="",
    val category: String="",




): Serializable
