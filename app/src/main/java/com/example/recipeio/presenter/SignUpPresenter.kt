package com.example.recipeio.presenter

import android.net.Uri
import com.example.recipeio.model.User
import com.example.recipeio.view.interfaces.SignUpView

interface SignUpPresenter {
    suspend fun signUp(user: User,uri: Uri?)
    fun attach(view:SignUpView)
}