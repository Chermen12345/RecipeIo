package com.example.recipeio.view.fragments.homefrs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentProfileSavedBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.presenter.AddToFavPresenterImpl
import com.example.recipeio.presenter.AddToFavView
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.adapters.RecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class ProfileSavedFragment : Fragment() , RecipeAdapter.OnClick,AddToFavView{
    //presenter
    private val presenter = AddToFavPresenterImpl()
    //binding
    private lateinit var binding: FragmentProfileSavedBinding

    //list of liked recipes
    private val likedList = arrayListOf<Recipe>()

    //liked recipes rc adapter
    private lateinit var adapter: RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSavedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init view
        presenter.attach(this)
        //db
        getLikedRecipes()
    }


    //TODO getting Liked recipes from db
    private fun getLikedRecipes(){
        REF.child("users/${AUTH.currentUser!!.uid}/savedRecipes").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    likedList.clear()
                    for (ds in snapshot.children){
                        val value = ds.getValue(Recipe::class.java)

                        value?.let { recipe ->
                            likedList.add(recipe)
                        }

                    }
                    adapter = RecipeAdapter(likedList,this@ProfileSavedFragment)
                    binding.apply {
                        rcsavedrecipes.adapter = adapter
                        rcsavedrecipes.layoutManager = GridLayoutManager(context,2)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    override fun isAtFav(recipe: Recipe): Boolean {
        if (likedList.contains(recipe)){
            return true
        }
        return false
    }



    //TODO adding to fav when checkbox was unchecked
    override fun onCheckBoxClickWhenUnChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.addToFav(recipe)
        }
    }

    //TODO removing from fav when checkbox was checked
    override fun onCheckBoxClickWhenChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.deleteFromFav(recipe)
        }
    }

    //TODO utils
    override fun message(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(recipe: Recipe) {
        val bundle = Bundle()

        bundle.putSerializable("recipe",recipe)

        bundle.putInt("nav_back",2)
        findNavController().navigate(R.id.action_profilefr_to_detailesFragment,bundle)
    }

    //when we click the users image we go to users profile
    override fun onImageClick(recipe: Recipe) {
        val bundle = Bundle()
        bundle.putSerializable("recipe",recipe)
        bundle.putInt("wherefrom",1)
        findNavController().navigate(R.id.action_profilefr_to_usersProfileFragment,bundle)
    }

}