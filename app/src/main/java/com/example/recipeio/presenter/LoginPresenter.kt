package com.example.recipeio.presenter




interface LoginPresenter {
    suspend fun login(email: String,password: String)
    suspend fun loginWithGoogle()
    fun attach(view: LoginView)
}
interface LoginView {
    fun goToHomeActivity()
    fun showProgress()
    fun message(string: String)
}