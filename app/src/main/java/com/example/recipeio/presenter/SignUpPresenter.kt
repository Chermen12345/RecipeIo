package com.example.recipeio.presenter

import android.net.Uri
import com.example.recipeio.model.User


interface SignUpPresenter {
    suspend fun signUp(user: User)
    fun attach(view:SignUpView)
}
interface SignUpView {
    fun goToHomeActivity()
    fun showProgress()
    fun message(string: String)
    fun hideProgress()

}