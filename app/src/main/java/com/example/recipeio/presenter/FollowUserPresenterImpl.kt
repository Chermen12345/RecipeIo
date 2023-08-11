package com.example.recipeio.presenter

import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import kotlin.random.Random

class FollowUserPresenterImpl():FollowUserPresenter {
    private lateinit var view: FollowUserView
    override suspend fun followUser(currentUser: User, user: User) {

        //adding the user to following
        REF.child("users/${AUTH.currentUser!!.uid}/following/${user.username}").setValue(user).addOnCompleteListener {
            if (it.isSuccessful){
                view.changeText()
            }
        }
        REF.child("users/${user.uid}/followers/${currentUser.username}").setValue(currentUser)
        REF.child("users/${user.uid}/notifications/${Random.nextInt()}").setValue(currentUser)

    }

    override suspend fun unFollowUser(currentUser: User, user: User) {
        //adding the user to following
        REF.child("users/${AUTH.currentUser!!.uid}/following/${user.username}").removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                view.changeBack()
            }
        }

        REF.child("users/${user.uid}/followers/${currentUser.username}").removeValue()

    }

    fun attach(view: FollowUserView){
        this.view = view
    }
}