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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentProfileRecipesBinding
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
import org.koin.android.ext.android.get


class ProfileRecipesFragment : Fragment(), RecipeAdapter.OnClick, AddToFavView{
    //presenter
    private val presenter = AddToFavPresenterImpl()

    private lateinit var binding: FragmentProfileRecipesBinding

    private val myRecipeList = arrayListOf<Recipe>()

    private lateinit var adapter: RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view init
        presenter.attach(this)
        //db

        getMyRecipes()
    }

    //TODO get own recipe from db
    private fun getMyRecipes(){

        REF.child("users/${AUTH.currentUser!!.uid}/myRecipes").addValueEventListener(

            object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    myRecipeList.clear()

                    for (ds in snapshot.children){

                        val value = ds.getValue(Recipe::class.java)
                        value?.let {recipe ->
                            myRecipeList.add(recipe)
                        }
                    }

                    adapter = RecipeAdapter(myRecipeList,this@ProfileRecipesFragment)
                    binding.apply {
                        rcownrecipes.adapter = adapter
                        rcownrecipes.layoutManager = GridLayoutManager(context,2)
                    }

                }



                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

    }



    override fun onItemClick(recipe: Recipe) {
        val bundle = Bundle()
        bundle.putSerializable("recipe",recipe)
        bundle.putInt("nav_back",2)
        findNavController().navigate(R.id.action_profilefr_to_detailesFragment,bundle)
    }

    override fun onCheckBoxClickWhenUnChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.addToFav(recipe)
        }
    }

    override fun onCheckBoxClickWhenChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.deleteFromFav(recipe)
        }
    }

    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}