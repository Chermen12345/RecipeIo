package com.example.recipeio.presenter

interface ForgotPassPresenter {
    suspend fun forgotPass(email: String)
    fun attach(forgotPassView: ForgotPassView)
}
interface ForgotPassView {
    fun goBack()
    fun message(string: String)
}