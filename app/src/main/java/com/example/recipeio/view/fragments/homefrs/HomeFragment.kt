package com.example.recipeio.view.fragments.homefrs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentHomeBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.presenter.AddToFavPresenterImpl
import com.example.recipeio.presenter.AddToFavView
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.FilterType
import com.example.recipeio.view.adapters.RecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class HomeFragment : Fragment(),RecipeAdapter.OnClick,AddToFavView{
    //presenter with all io functions
    private val presenter = AddToFavPresenterImpl()
    //binding init
    private lateinit var binding: FragmentHomeBinding
    //adapter init
    private lateinit var adapter: RecipeAdapter
    //list for adapter in usual state
    private val list = arrayListOf<Recipe>()
    //list for adapter when user filtered by category
    private var byCategoryList: List<Recipe> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)


        //getting info from db
        getRecipes()

        //filtering recipes
        searchRecipes()
        getByCategoryClick()

    }

    //TODO filtering recipes

    //here we filter recipes by text which user input(searching)
    private fun searchRecipes() {
        binding.apply {
            searchRecipes.setOnQueryTextListener(object : OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!=null){
                        val filteredlist = list.filter {recipe ->
                            recipe.foodName.toLowerCase().contains(newText)||
                                    recipe.description.toLowerCase().contains(newText)||
                                    recipe.username.toLowerCase().contains(newText)||
                                    recipe.description.toLowerCase().contains(newText)
                        }
                        val filteredAdapter = RecipeAdapter(filteredlist as ArrayList<Recipe>,this@HomeFragment)
                        binding.rcRecipes.adapter = filteredAdapter
                    }
                    return true
                }

            })
        }
    }
    //here we are checking which category was chosen by user. with enum class we check which category
    //was chosen and in this case we filter list of our adapter by current category

    private fun checkStateOfCategory(type: FilterType){
        when(type){

            FilterType.Food -> {
                byCategoryList = list.filter {recipe ->
                    recipe.category.toLowerCase().contains("food")
                }
                adapter = RecipeAdapter(byCategoryList as ArrayList<Recipe>,this)
                binding.rcRecipes.adapter = adapter
            }
            FilterType.Drink -> {
                byCategoryList = list.filter {recipe ->
                    recipe.category.toLowerCase().contains("drink")
                }
                adapter = RecipeAdapter(byCategoryList as ArrayList<Recipe>,this)
                binding.rcRecipes.adapter = adapter
            }
            else -> {
                adapter = RecipeAdapter(list,this)
                binding.rcRecipes.adapter = adapter
            }

        }
    }

    //here we execute previous function with current name of enum class or our filter type
    //by clicking on the current button
    private fun getByCategoryClick(){
        binding.apply {
            btdrink.setOnClickListener {
                checkStateOfCategory(FilterType.Drink)

                btdrink.setBackgroundResource(R.drawable.button1)
                btfood.setBackgroundResource(R.drawable.button3)
                btall.setBackgroundResource(R.drawable.button3)


            }
            btfood.setOnClickListener {
                checkStateOfCategory(FilterType.Food)

                btfood.setBackgroundResource(R.drawable.button1)
                btdrink.setBackgroundResource(R.drawable.button3)
                btall.setBackgroundResource(R.drawable.button3)
            }
            btall.setOnClickListener {
                checkStateOfCategory(FilterType.Whole)

                btall.setBackgroundResource(R.drawable.button1)
                btfood.setBackgroundResource(R.drawable.button3)
                btdrink.setBackgroundResource(R.drawable.button3)
            }
        }

    }


    //TODO getting from db


    //here we are getting all the recipes from real time database
    private fun getRecipes(){
        REF.child("recipes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children){
                    val value = ds.getValue(Recipe::class.java)
                    if (value!=null){
                        list.add(0,value)
                    }

                }
                adapter = RecipeAdapter(list,this@HomeFragment)
                binding.rcRecipes.adapter = adapter
                binding.rcRecipes.layoutManager = GridLayoutManager(context,2)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onItemClick(recipe: Recipe) {
        val bundle = Bundle()
        bundle.putSerializable("recipe",recipe)
        bundle.putInt("nav_back",1)
        findNavController().navigate(R.id.action_homefr_to_detailesFragment,bundle)

    }

    override fun onCheckBoxClickWhenUnChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.addToFav(recipe)
        }

    }


    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


}