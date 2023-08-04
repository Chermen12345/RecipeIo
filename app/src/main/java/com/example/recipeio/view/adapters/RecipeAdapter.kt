package com.example.recipeio.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeio.databinding.OneRecipeBinding
import com.example.recipeio.model.Recipe

class RecipeAdapter(val list: ArrayList<Recipe>,val  onClick: OnClick): RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {
    inner class RecipeHolder(val binding: OneRecipeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        return RecipeHolder(OneRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        Glide.with(holder.itemView).load(list[position].image).into(holder.binding.imgrecipehome)
        holder.binding.tvdescriptionhome.text = "description: ${list[position].description}"
        holder.binding.tvnamehome.text = list[position].username
        Glide.with(holder.itemView).load(list[position].userImage).into(holder.binding.imgprofilehome)
        holder.binding.tvfoodname.text = list[position].foodName

        holder.itemView.setOnClickListener {
            onClick.onItemClick(list[position])
        }
        holder.binding.checkBox2.setOnClickListener {
            holder.binding.checkBox2.apply {
                when(isChecked){
                    false->{onClick.onCheckBoxClick(list[position],isChecked)}
                    true->{onClick.onCheckBoxClick(list[position],isChecked)}
                }
            }
        }


    }

    interface OnClick{
        fun onItemClick(recipe: Recipe)
        fun onCheckBoxClick(recipe: Recipe,wasAtFav: Boolean)



    }
}