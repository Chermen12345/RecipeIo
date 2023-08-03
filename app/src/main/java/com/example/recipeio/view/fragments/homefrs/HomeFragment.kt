package com.example.recipeio.view.fragments.homefrs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentHomeBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.utils.FilterType
import com.example.recipeio.view.adapters.RecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecipeAdapter
    private val list = arrayListOf<Recipe>()
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
        getRecipes()
        getByCategoryClick()

    }
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
                adapter = RecipeAdapter(list)
                binding.rcRecipes.adapter = adapter
                binding.rcRecipes.layoutManager = GridLayoutManager(context,2)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkStateOfCategory(type: FilterType){
        when(type){

            FilterType.Food -> {
                byCategoryList = list.filter {recipe ->
                    recipe.category.toLowerCase().contains("food")
                }
                adapter = RecipeAdapter(byCategoryList as ArrayList<Recipe>)
                binding.rcRecipes.adapter = adapter
            }
            FilterType.Drink -> {
                byCategoryList = list.filter {recipe ->
                    recipe.category.toLowerCase().contains("drink")
                }
                adapter = RecipeAdapter(byCategoryList as ArrayList<Recipe>)
                binding.rcRecipes.adapter = adapter
            }
            else -> {
                adapter = RecipeAdapter(list)
                binding.rcRecipes.adapter = adapter
            }

        }
    }
    @SuppressLint("ResourceAsColor")
    private fun getByCategoryClick(){
        binding.apply {
            btdrink.setOnClickListener {
                checkStateOfCategory(FilterType.Drink)

                btdrink.setBackgroundResource(R.drawable.button1)
                btfood.setBackgroundResource(R.drawable.button3)
                btall.setBackgroundResource(R.drawable.button3)

                btdrink.setTextColor(R.color.white)
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


}