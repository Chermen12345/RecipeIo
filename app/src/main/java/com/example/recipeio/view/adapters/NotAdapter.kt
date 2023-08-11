package com.example.recipeio.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeio.databinding.OneNotificationBinding
import com.example.recipeio.databinding.OneUserBinding
import com.example.recipeio.model.User

class NotAdapter(val listOfNots: ArrayList<User>, val click: NotAdapter.OnClick): RecyclerView.Adapter<NotAdapter.NotHolder>(){
    inner class NotHolder(val binding: OneNotificationBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotHolder {
        return NotHolder(OneNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listOfNots.size
    }

    override fun onBindViewHolder(holder: NotHolder, position: Int) {
        Glide.with(holder.itemView).load(listOfNots[position].uri).into(holder.binding.imgnotification)
        holder.binding.tvnamenot.text = listOfNots[position].username

        holder.itemView.setOnClickListener {
            click.onNotClick(listOfNots[position])
        }
    }
    interface OnClick{
        fun onNotClick(user: User)
    }
}