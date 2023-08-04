package com.example.recipeio.presenter

import com.example.recipeio.model.Recipe

interface AddToFavouritesPresenter {
    suspend fun addToFav(recipe: Recipe)
    suspend fun deleteFromFav(recipe: Recipe)
    fun attach(view: AddToFavView)
}
interface AddToFavView{
    fun message(message: String)
}