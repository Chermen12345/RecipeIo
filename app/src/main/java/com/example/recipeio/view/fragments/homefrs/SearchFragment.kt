package com.example.recipeio.view.fragments.homefrs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentSearchBinding
import com.example.recipeio.model.Recipe
import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.adapters.UserSearchAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment(),UserSearchAdapter.onClick {

    private lateinit var binding: FragmentSearchBinding

    lateinit var adapter: UserSearchAdapter

    var list = arrayListOf<User>()
    var searchList = arrayListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ui
        hideImageAndTv()
        showImageAndTv()

        //db
        getUsers()
        searchUsers()
    }

    //TODO ui

    //hide imageview and text view for info, show recycler view with users
    private fun hideImageAndTv(){
        binding.searchUsers.setOnSearchClickListener {
            binding.imgsearch.visibility = View.GONE
            binding.tvsearch.visibility = View.GONE
            binding.rcUsers.visibility = View.VISIBLE
        }
    }
    //show imageview and text view for info, hide recycler view with users
    private fun showImageAndTv(){
        binding.apply {
            imgsearch.visibility = View.VISIBLE
            binding.tvsearch.visibility = View.VISIBLE
            binding.rcUsers.visibility = View.GONE
        }
    }
    //TODO db

    //get Users from db
    private fun getUsers(){
        REF.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children){
                    val value = ds.getValue(User::class.java)
                    if (value!=null){
                        list.add(value)
                    }

                }
                binding.apply {
                    adapter = UserSearchAdapter(list,this@SearchFragment)
                    rcUsers.adapter = adapter
                    rcUsers.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun searchUsers(){
        binding.searchUsers.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null){
                    searchList = list.filter { user ->
                        user.username.toLowerCase().contains(newText)
                    } as ArrayList<User>
                    adapter = UserSearchAdapter(searchList,this@SearchFragment)
                    binding.rcUsers.adapter = adapter
                }
                return true
            }
        })
    }

    override fun onUserClick(user: User) {
        val recipe = Recipe(ownerId = user.uid, userImage = user.uri, username = user.username)
        val bundle = Bundle()
        bundle.putSerializable("recipe",recipe)
        bundle.putInt("wherefrom",1)
        findNavController().navigate(R.id.action_searchfr_to_usersProfileFragment,bundle)
    }
}