package com.example.recipeio.view.fragments.homefrs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentProfileBinding
import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.adapters.ProfileViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    //list of tablayout items
    private val tabList = listOf<String>("Recipes","Liked")


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

        getUserInfo()


        viewPager()
    }


    //TODO get from db

    //get User Info from real time database
    private fun getUserInfo(){
        REF.child("users/${AUTH.currentUser!!.uid}").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                if (user!=null){
                    binding.apply {
                        tvusernameprofile.text = user.username
                        Glide.with(this@ProfileFragment).load(user.uri).into(imgprofilefr)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

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




}