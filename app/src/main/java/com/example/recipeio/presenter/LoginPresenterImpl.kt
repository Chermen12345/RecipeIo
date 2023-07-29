package com.example.recipeio.presenter

import com.example.recipeio.Consts.AUTH
import com.example.recipeio.Consts.REF


class LoginPresenterImpl(): LoginPresenter {
    private lateinit var loginView: LoginView
    override suspend fun login(email: String, password: String) {
        if (email.isNotEmpty()&&password.isNotEmpty()){
            loginView.showProgress()
            AUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->
                if (task.isSuccessful){
                    val hashMap = hashMapOf<String,Any>()
                    hashMap["password"] = password
                    REF.child("users/${AUTH.currentUser!!.uid}").updateChildren(hashMap)
                    loginView.message("sign in successfully")
                    loginView.goToHomeActivity()

                }else{
                    loginView.message(task.exception!!.message.toString())
                }
            }
        }else{
            loginView.message("your password or email is empty")
        }
    }

    override suspend fun loginWithGoogle() {

    }

    override fun attach(loginView: LoginView) {
        this.loginView = loginView
    }


}