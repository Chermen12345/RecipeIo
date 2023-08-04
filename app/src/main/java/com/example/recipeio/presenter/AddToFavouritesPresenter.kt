package com.example.recipeio.presenter

import com.example.recipeio.model.Recipe

interface AddToFavouritesPresenter {
    suspend fun addToFavOrDelete(recipe: Recipe,wasAtFav: Boolean)

    fun attach(view: AddToFavView)
}
interface AddToFavView{
    fun message(message: String)
}