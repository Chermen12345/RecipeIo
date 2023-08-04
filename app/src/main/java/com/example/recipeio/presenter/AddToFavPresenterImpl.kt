package com.example.recipeio.presenter

import com.example.recipeio.model.Recipe
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF

class AddToFavPresenterImpl(): AddToFavouritesPresenter {
    private lateinit var view: AddToFavView

    override suspend fun addToFavOrDelete(recipe: Recipe,wasAtFav:Boolean) {
        val key = REF.push().key
        if (wasAtFav){
            REF.child("users/${AUTH.currentUser!!.uid}/savedRecipes/$key").removeValue()
                .addOnCompleteListener {

                    if (it.isSuccessful){
                        view.message("removed from saved successfully")
                    }else{
                        view.message(it.exception!!.message.toString())
                    }

                }
        }else{
            REF.child("users/${AUTH.currentUser!!.uid}/savedRecipes/$key").setValue(recipe)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        view.message("saved successfully")
                    }else{
                        view.message(it.exception!!.message.toString())
                    }
                }


        }


    }



    override fun attach(view: AddToFavView) {
        this.view = view
    }
}