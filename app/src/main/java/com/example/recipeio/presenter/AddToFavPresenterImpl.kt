package com.example.recipeio.presenter

import com.example.recipeio.model.Recipe
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF

class AddToFavPresenterImpl(): AddToFavouritesPresenter {
    private lateinit var view: AddToFavView
    override suspend fun addToFav(recipe: Recipe) {
        REF.child("users/${AUTH.currentUser!!.uid}/savedRecipes").push().setValue(recipe)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    view.message("saved successfully")
                }else{
                    view.message(it.exception!!.message.toString())
                }
            }
    }

    override suspend fun deleteFromFav(recipe: Recipe) {

    }

    override fun attach(view: AddToFavView) {
        this.view = view
    }
}