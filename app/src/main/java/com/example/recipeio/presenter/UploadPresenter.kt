package com.example.recipeio.presenter

import android.net.Uri
import com.example.recipeio.model.Recipe

interface UploadPresenter {
    suspend fun insertRecipe(recipe: Recipe)
}
interface UploadView{
    fun close()
    fun message(string: String)

    fun hideProgress()
    fun showProgress()
}