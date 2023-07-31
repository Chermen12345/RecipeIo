package com.example.recipeio.presenter

import android.net.Uri

import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.Consts.STORAGE


class SignUpPresenterImpl(): SignUpPresenter {
    private lateinit var view: SignUpView

    override suspend fun signUp(user: User,uri: Uri) {
        user.apply {
            if (email.isNotEmpty()&&password.isNotEmpty()&&repeatPassword.isNotEmpty()){
                if (password.equals(repeatPassword)){
                    view.showProgress()
                    AUTH.createUserWithEmailAndPassword(email, password).addOnCompleteListener { emailreg->
                        if (emailreg.isSuccessful){
                            REF.child("users/${AUTH.currentUser!!.uid}").setValue(user).addOnCompleteListener {refreg->
                                if (refreg.isSuccessful){
                                    STORAGE.child("users/profileimages/${AUTH.currentUser!!.uid}").putFile(uri).addOnSuccessListener {
                                        view.message("sign up successfully")
                                        view.goToHomeActivity()
                                    }.addOnFailureListener{
                                        view.message(it.message.toString())
                                    }
                                }else{
                                    view.message(refreg.exception!!.message.toString())
                                }
                            }
                        }else{
                            view.message(emailreg.exception!!.message.toString())
                        }
                    }
                }else{
                    view.message("your passwords are not the same")
                }
            }else{
                view.message("please, fill all the fields")
            }
        }
    }

    override fun attach(view: SignUpView) {
        this.view = view
    }

}