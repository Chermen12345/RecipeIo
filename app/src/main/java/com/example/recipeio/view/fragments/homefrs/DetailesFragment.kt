package com.example.recipeio.view.fragments.homefrs

import android.graphics.BlurMaskFilter.Blur
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentDetailesBinding
import com.example.recipeio.model.Recipe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.get


class DetailesFragment : Fragment() {
    private lateinit var binding: FragmentDetailesBinding
    private var recipe: Recipe?=null
    private var nav_back: Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        putData()


        setUpBottomSheetDialog()

        goBack()
    }

    private fun getData(){
        val args = arguments

        recipe = args!!.getSerializable("recipe") as Recipe
        nav_back = args.getInt("nav_back")
    }
    private fun putData(){
        binding.apply {
            Glide.with(this@DetailesFragment).load(recipe!!.image).into(imgDetail)
            Glide.with(this@DetailesFragment).load(recipe!!.userImage).into(imguserdet)
            tvcategorydet.text = recipe!!.category
            tvdescdet.text = recipe!!.description
            tvingrdet.text = recipe!!.ingredients
            tvfoodnamedetail.text = recipe!!.foodName
            tvusernamedet.text = recipe!!.username
        }
    }

    private fun setUpBottomSheetDialog(){
        BottomSheetBehavior.from(binding.btsheet).apply {
            peekHeight = 850
        }
    }

    private fun goBack(){
        binding.btbackdet.setOnClickListener {
            when(nav_back){
                1 -> {findNavController().navigate(R.id.action_detailesFragment_to_homefr)}
            }
        }

    }


}