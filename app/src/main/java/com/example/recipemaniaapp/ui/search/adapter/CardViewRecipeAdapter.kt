package com.example.recipemaniaapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import kotlinx.android.synthetic.main.item_cardview_recipe.view.*

class CardViewRecipeAdapter(private val listRecipe: ArrayList<Recipe>) :
    RecyclerView.Adapter<CardViewRecipeAdapter.CardViewViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_cardview_recipe, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int =  listRecipe.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(recipe.photo)
                    .apply(RequestOptions().override(350, 550))
                    .into(recipe_image)

                tv_recipe_name.text = recipe.name + " Recipe"

                tv_like_count.text = recipe.like.toString() + " Likes"

                like_button.setOnClickListener {
                    Toast.makeText(itemView.context, "Like ${recipe.name}", Toast.LENGTH_SHORT).show()
                }

                save_btn.setOnClickListener{
                    Toast.makeText(itemView.context, "Save ${recipe.name}", Toast.LENGTH_SHORT).show() }

                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "Show ${recipe.name}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listRecipe[position])
    }




}