package com.example.recipeio.presenter

import com.example.recipeio.model.User

interface FollowUserPresenter {
    suspend fun followUser(currentUser: User,user: User)
    suspend fun unFollowUser(currentUser: User,user: User)
}
interface FollowUserView{
    fun changeText()
    fun changeBack()
}