package com.example.recipemaniaapp.ui.home.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.db.LikeHelper
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.home.HomeFeed
import com.example.recipemaniaapp.ui.home.RecipeDetail.InformationFragment
import com.example.recipemaniaapp.ui.home.RecipeDetail.IngredientsFragment
import com.example.recipemaniaapp.ui.home.RecipeDetail.RecipeDetailActivity
import com.example.recipemaniaapp.ui.home.RecipeDetail.StepsFragment
import com.example.recipemaniaapp.ui.home.Recipes
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home_recipe_row.view.recipe_image
import kotlinx.android.synthetic.main.fragment_home_recipe_row.view.tv_recipe_name

class MainAdapter(val homeFeed: HomeFeed): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

//    val recipeTitles = listOf("First", "Second", "Third")


    //numberOfItems
    override fun getItemCount(): Int {
        return homeFeed.results.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_home_recipe_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val recipeTitles = recipeTitles.get(position)
        val recipe = homeFeed.results.get(position)
        holder.view.tv_recipe_name.text = recipe.title
        //getImage
        val url = "https://spoonacular.com/recipeImages/"
        val id = recipe.id
        val size = "636x393"
        val type = ".jpg"
        val imageUrl = url + id + "-" + size + type
        val thumbnailImageView = holder.view.recipe_image
        Picasso.get().load(imageUrl).into(thumbnailImageView)

        holder?.recipe = recipe
    }
    class CustomViewHolder(val view: View, var recipe: Recipes? = null): RecyclerView.ViewHolder(view){
        companion object{
            val RECIPE_ID_KEY = "RECIPE_ID"
        }
//        private lateinit var likeHelper: LikeHelper
//        lateinit var likeBtn : ImageButton
//
//        fun bind(recipe: Recipe){
//            with(view){
////                likeBtn = findViewById(R.id.like_button)
////                likeHelper = LikeHelper.getInstance(activity.applicationContext)
////                likeHelper.open()
////
////                val databaseRef = FirebaseDatabase.getInstance().reference
//
//            }
//
//        }
        init {
            view.setOnClickListener {
                val intent = Intent(view.context, RecipeDetailActivity::class.java)

                intent.putExtra(RECIPE_ID_KEY, recipe?.id)

//                var data = Bundle()
//                data.putInt(InformationFragment.RECIPE_ID, recipe!!.id)
//                val infoFragment = InformationFragment()
//                val ingredientsFragment = IngredientsFragment()
//                val stepsFragment = StepsFragment()
//
//                infoFragment.arguments = data
//                ingredientsFragment.arguments = data
//                stepsFragment.arguments = data

                view.context.startActivity(intent)
            }
        }
    }

}
