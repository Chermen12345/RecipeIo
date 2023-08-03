package com.example.recipeio.presenter

import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.recipeio.model.Recipe
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.Consts.STORAGE
import org.koin.core.definition.indexKey

class UploadPresenterImpl(): UploadPresenter {
    lateinit var view: UploadView
    override suspend fun insertRecipe(recipe: Recipe) {
        val key = REF.push().key
        if (recipe.foodName.isNotEmpty()&&recipe.ingredients.isNotEmpty()&&recipe.description.isNotEmpty()){
            if (recipe.image!=null){
                view.showProgress()
                STORAGE.child("recipeImages/$key").putFile(recipe.image.toUri()).addOnCompleteListener{stortask->
                    if (stortask.isSuccessful){
                        STORAGE.child("recipeImages/$key").downloadUrl.addOnSuccessListener {uri->
                            val map = hashMapOf<String,String>()
                            map["ownerId"] = AUTH.currentUser!!.uid
                            map["foodName"] = recipe.foodName
                            map["description"] = recipe.description
                            map["ingredients"] = recipe.ingredients
                            map["image"] = uri.toString()
                            map["userImage"] = recipe.userImage
                            map["username"] = recipe.username
                            map["category"] = recipe.category
                            REF.child("recipes/$key").setValue(map).addOnCompleteListener {reftask->
                                if (reftask.isSuccessful){
                                    view.message("uploaded successfully")
                                    view.close()
                                }else{
                                    view.hideProgress()
                                    view.message(reftask.exception!!.message.toString())
                                }
                            }
                        }.addOnFailureListener {
                            view.hideProgress()
                            view.message(it.message.toString())
                        }
                    }else{
                        view.hideProgress()
                        view.message(stortask.exception!!.message.toString())
                    }
                }
            }else{
                view.message("please choose an image")
            }
        }else{
            view.message("please, fill all places")
        }
    }
    fun attach(view: UploadView){
        this.view = view
    }

}