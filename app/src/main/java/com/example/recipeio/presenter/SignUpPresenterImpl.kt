package com.example.recipeio.presenter

import android.net.Uri
import com.example.recipeio.Consts.AUTH
import com.example.recipeio.Consts.REF
import com.example.recipeio.Consts.STORAGE
import com.example.recipeio.model.User
import com.example.recipeio.view.interfaces.SignUpView

class SignUpPresenterImpl(): SignUpPresenter {
    private lateinit var view: SignUpView
    override suspend fun signUp(user: User,uri: Uri?) {
        if (user.email.isNotEmpty()&&user.password.isNotEmpty()){
            if (user.password.equals(user.repeatPassword)){
                AUTH.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {emailandpass->
                    if (emailandpass.isSuccessful){
                        REF.child("users/${AUTH.currentUser!!.uid}").setValue(user).addOnSuccessListener {
                        if (uri!=null){
                            STORAGE.child(AUTH.currentUser!!.uid).putFile(uri!!).addOnSuccessListener {
                                view.message("Sign Up successfully")
                                view.goToHomeActivity()
                            }.addOnFailureListener{
                                view.message(it.message.toString())
                            }
                        }else{
                            view.message("please, choose the profile image")
                        }

                        }.addOnFailureListener {
                            view.message(it.message.toString())
                        }
                    }else{
                        view.message(emailandpass.exception!!.message.toString())
                    }
                }
            }else{
                view.message("your passwords are not the same")
            }
        }else{
            view.message("something is empty")
        }
    }

    override fun attach(view: SignUpView) {
        this.view = view
    }

}