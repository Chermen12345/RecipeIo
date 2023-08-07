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
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentUsersProfileBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.model.User
import com.example.recipeio.presenter.AddToFavPresenterImpl
import com.example.recipeio.presenter.AddToFavView
import com.example.recipeio.presenter.FollowUserPresenterImpl
import com.example.recipeio.presenter.FollowUserView
import com.example.recipeio.utils.Consts
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.adapters.RecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UsersProfileFragment : Fragment(),RecipeAdapter.OnClick,AddToFavView,FollowUserView {
    private lateinit var binding: FragmentUsersProfileBinding
    private val presenter = AddToFavPresenterImpl()
    private var recipe: Recipe ?= null
    private var wherefrom: Int ?= null

    private lateinit var adapter: RecipeAdapter
    private val ownRecipesList = arrayListOf<Recipe>()
    private val listOfSavedRecipes = arrayListOf<Recipe>()

    private val followPresenter = FollowUserPresenterImpl()

    private var user: User ?= null
    private var currentUser: User ?= null

    private var recipesCount:Int?=null

    private val followingList = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        followPresenter.attach(this)

        getInfo()
        putInfoToUi()
        whereFrom()
        getUserRecipes()
        getCurrentUser()
        binding.apply {
            lifecycleScope.launch {
                delay(1000)
                if (btFollow.text=="Follow"){
                    follow()
                }else if (btFollow.text == "Unfollow"){
                    unFollow()
                }
            }
        }


        isAlreadyFollowed()

        lifecycleScope.launch {
            async {
                userInit()
                delay(1)
                getCountFollowersAndFollowing()
                delay(1)
                getCountOfOwnRecipes()
            }

        }

    }
    private suspend fun userInit(){
        user = User(uid = recipe!!.ownerId, username = recipe!!.username
            , uri = recipe!!.userImage)
    }

    //getting users info
    private fun getInfo(){
        val args = arguments
        args?.let {
            recipe = it.getSerializable("recipe") as Recipe
            wherefrom = it.getInt("wherefrom")
        }
    }


    //here we put All Info To Ui
    private fun putInfoToUi(){
        binding.apply {
        recipe?.let { recipe
            tvusernameuserprofile.text = recipe!!.username
            Glide.with(this@UsersProfileFragment).load(recipe!!.userImage).into(imgusersprofilefr)
        }

        }
    }
    //here we check from which fragment we came to users profile fragment to go back to the current fr
    private fun whereFrom(){
        binding.btBackFromUsersProfile.setOnClickListener {
            when(wherefrom){
                1->{findNavController().navigate(R.id.action_usersProfileFragment_to_homefr)}
                2->{val bundle = Bundle()
                    bundle.putSerializable("recipe",recipe)
                    bundle.putInt("nav_back",1)
                    findNavController().navigate(R.id.action_usersProfileFragment_to_detailesFragment,bundle)}
            }
        }
    }
    //getting all recipes of alien user
    private fun getUserRecipes(){
        REF.child("users/${recipe!!.ownerId}/myRecipes").addValueEventListener(

            object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    ownRecipesList.clear()
                    for (ds in snapshot.children){
                        val value = ds.getValue(Recipe::class.java)
                        value?.let { recipe ->
                            ownRecipesList.add(recipe)
                        }
                    }
                    adapter = RecipeAdapter(ownRecipesList,this@UsersProfileFragment)
                    binding.rcOwnRecipes.adapter = adapter
                    binding.rcOwnRecipes.layoutManager = GridLayoutManager(context,2)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

        )
    }


    //here we get the whole recipes that we liked to check in all items contain saved items to show
    //highlighted checkbox
    private fun getLikedRecipes(){
        REF.child("users/${Consts.AUTH.currentUser!!.uid}/savedRecipes").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfSavedRecipes.clear()
                    for (ds in snapshot.children){
                        val value = ds.getValue(Recipe::class.java)

                        value?.let { listOfSavedRecipes.add(it) }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
    //here we are checking if the list of recipes that we liked containg current recipe in rcview
    //we need to check it to make the check box checked in item that we liked
    // and to make current check box unchecked when we didnt saved it
    override fun isAtFav(recipe: Recipe): Boolean {
        if (listOfSavedRecipes.contains(recipe)){
            return true
        }
        return false
    }


    //here we go to detail fragment
    override fun onItemClick(recipe: Recipe) {
        val bundle = Bundle()
        bundle.putSerializable("recipe",recipe)
        bundle.putInt("nav_back",3)
        findNavController().navigate(R.id.action_usersProfileFragment_to_detailesFragment,bundle)

    }

    //here when the checkbox is unchecked we add to fav clicking the checkbox
    override fun onCheckBoxClickWhenUnChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.addToFav(recipe)
        }

    }


    //follow when clicking the button
    private fun follow(){
        binding.btFollow.setOnClickListener {

            lifecycleScope.launch {
                if (currentUser!!.uid!=user!!.uid){
                    followPresenter.followUser(currentUser!!, user!!)
                }else{
                    Toast.makeText(context,"you cant follow for your account",
                    Toast.LENGTH_LONG).show()
                }

                delay(100)
                if (binding.btFollow.text=="Follow"){
                    follow()
                }else if (binding.btFollow.text == "Unfollow"){
                    unFollow()
                }
            }

        }
    }
    //follow when clicking the button
    private fun unFollow(){
        binding.btFollow.setOnClickListener {

            lifecycleScope.launch {
                    followPresenter.unFollowUser(currentUser!!, user!!)


                delay(100)
                if (binding.btFollow.text=="Follow"){
                    follow()
                }else if (binding.btFollow.text == "Unfollow"){
                    unFollow()
                }
            }


        }
    }
    private fun getCurrentUser(){
        REF.child("users/${AUTH.currentUser!!.uid}").addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUser = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    private suspend fun getCountFollowersAndFollowing(){
        REF.child("users/${user!!.uid}/followers").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count()
                    binding.tvuserfollowers.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


        REF.child("users/${user!!.uid}/following").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.children.count()
                    binding.tvuserfollowing.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
    private suspend fun getCountOfOwnRecipes(){
        lifecycleScope.launch {
            REF.child("users/${user!!.uid}/myRecipes").addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recipesCount = snapshot.children.count()
                        binding.tvamountrecipesuser.text = recipesCount.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        }

    }

    //here we check if user Already followed
    private fun isAlreadyFollowed(){
        REF.child("users/${AUTH.currentUser!!.uid}/following").addValueEventListener(object :
        ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                followingList.clear()
                for (ds in snapshot.children){
                    val value = ds.getValue(User::class.java)
                    value?.let {
                        followingList.add(it)
                    }
                    if (followingList.contains(user)){
                        binding.btFollow.text = "Unfollow"
                    }else{
                        binding.btFollow.text = "Follow"
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }





    //here when checkbox was checked we delete from fav by clicking the checkbox
    override fun onCheckBoxClickWhenChecked(recipe: Recipe) {
        lifecycleScope.launch {
            presenter.deleteFromFav(recipe)
        }
    }

    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    override fun changeText() {
        binding.btFollow.setText("Unfollow")
    }

    override fun changeBack() {
        binding.btFollow.setText("Follow")
    }

}