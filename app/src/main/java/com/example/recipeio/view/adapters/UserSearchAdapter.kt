package com.example.recipeio.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeio.databinding.OneUserBinding
import com.example.recipeio.model.User

class UserSearchAdapter(val listOfUsers: ArrayList<User>): RecyclerView.Adapter<UserSearchAdapter.UserHolder>(){
    inner class UserHolder(val binding: OneUserBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(OneUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listOfUsers.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        Glide.with(holder.itemView).load(listOfUsers[position].uri).into(holder.binding.imgusersearch)
        holder.binding.tvusernamesearch.text = listOfUsers[position].username
    }
}