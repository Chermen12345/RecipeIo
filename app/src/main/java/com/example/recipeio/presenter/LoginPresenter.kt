package com.example.recipeio.presenter

import com.example.recipeio.view.interfaces.LoginView


interface LoginPresenter {
    suspend fun login(email: String,password: String)
    suspend fun loginWithGoogle()
    fun attach(view: LoginView)
}