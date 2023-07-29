package com.example.recipeio.presenter

import com.example.recipeio.Consts.AUTH
import com.example.recipeio.Consts.REF

class ForgotPassPresenterImpl():ForgotPassPresenter {
    lateinit var view: ForgotPassView
    override suspend fun forgotPass(email: String) {
        if (email.isNotEmpty()){
            AUTH.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    view.message("check your email,we have sent you a letter to change password")
                    view.goBack()
                }else{
                    view.message(it.exception!!.message.toString())
                }
            }
        }else{
            view.message("please, write your email")
        }
    }

    override fun attach(forgotPassView: ForgotPassView) {
        this.view = forgotPassView
    }
}