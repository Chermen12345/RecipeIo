package com.example.recipeio.presenter

import android.net.Uri
import androidx.core.net.toUri

import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.Consts.STORAGE


class SignUpPresenterImpl(): SignUpPresenter {
    private lateinit var view: SignUpView

    override suspend fun signUp(user: User) {
        user.apply {
            if (email.isNotEmpty()&&password.isNotEmpty()&&repeatPassword.isNotEmpty()&&username.isNotEmpty()){
                view.showProgress()
                AUTH.createUserWithEmailAndPassword(email, password).addOnCompleteListener {emAuth->
                if (emAuth.isSuccessful){
                    STORAGE.child("users/profileImages/${AUTH.currentUser!!.uid}").putFile(uri.toUri()).addOnCompleteListener{putimg->
                        if (putimg.isSuccessful){
                            STORAGE.child("users/profileImages/${AUTH.currentUser!!.uid}").downloadUrl.addOnSuccessListener {url->
                                val map = hashMapOf<String,String>()
                                map["username"] = username
                                map["email"] = email
                                map["password"] = password
                                map["repeatPassword"] = repeatPassword
                                map["uri"] = url.toString()
                                REF.child("users/${AUTH.currentUser!!.uid}").setValue(map).addOnCompleteListener {ref->
                                    if (ref.isSuccessful){
                                        view.hideProgress()
                                        view.message("sign up successfully")
                                        view.goToHomeActivity()
                                    }else{
                                        view.hideProgress()
                                        view.message(ref.exception!!.message.toString())
                                    }
                                }
                            }
                        }else{

                            view.hideProgress()
                          view.message(putimg.exception!!.message.toString())
                        }
                    }
                }else{
                    view.hideProgress()
                    view.message(emAuth.exception!!.message.toString())
                }

                }
            }else{
                view.message("you should fill all places")
            }
        }
    }

    override fun attach(view: SignUpView) {
        this.view = view
    }

}