package com.example.recipeio.presenter

import android.net.Uri
import com.example.recipeio.model.User


interface SignUpPresenter {
    suspend fun signUp(user: User,uri: Uri)
    fun attach(view:SignUpView)
}
interface SignUpView {
    fun goToHomeActivity()
    fun showProgress()
    fun message(message: String)

}