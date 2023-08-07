package com.example.recipeio.view.fragments.homefrs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentProfileBinding
import com.example.recipeio.model.User
import com.example.recipeio.presenter.FollowUserView
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.adapters.ProfileViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    //list of tablayout items
    private val tabList = listOf<String>("Recipes","Liked")

    private lateinit var profileImage: String
    private lateinit var profileName: String

    private var recipesCount:Int ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            async {
                getUserInfo()
            }.await()
            delay(200)
            putInfoUi()
        }


        getCountOfOwnRecipes()




        viewPager()

        lifecycleScope.launch {
            getCountFollowersAndFollowing()
        }
    }


    //TODO get from db

    //get User Info from real time database
    private suspend fun getUserInfo(){
        REF.child("users/${AUTH.currentUser!!.uid}").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                if (user!=null){
                    binding.apply {
                        profileName = user.username
                        profileImage = user.uri
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    //put the profile info to ui
    private suspend fun putInfoUi(){
        Glide.with(this).load(profileImage).into(binding.imgprofilefr)
        binding.tvusernameprofile.text = profileName
    }

    //TODO setUp ViewPager
    private fun viewPager(){
        binding.apply {
            pager.adapter = ProfileViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
            TabLayoutMediator(tab,pager){
                tab,pos-> tab.text = tabList[pos]
            }.attach()

        }
    }

    private fun getCountOfOwnRecipes(){
        lifecycleScope.launch {
            REF.child("users/${AUTH.currentUser!!.uid}/myRecipes").addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recipesCount = snapshot.children.count()
                        binding.tvamountrecipes.text = recipesCount.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        }

    }


    //get count
    private suspend fun getCountFollowersAndFollowing(){
        REF.child("users/${AUTH.currentUser!!.uid}/followers").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count()
                    binding.tvamountfollowers.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


        REF.child("users/${AUTH.currentUser!!.uid}/following").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count()
                    binding.tvamountoffollowing.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }




}