package com.example.recipeio.presenter

interface Presenter {
    suspend fun login(email: String,password: String)
}