package com.example.recipemaniaapp.ui.home.RecipeDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.home.RecipeDetailAppBar
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter
import com.example.recipemaniaapp.ui.search.adapter.CardViewRecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_detail_tab_layout.*
import kotlinx.android.synthetic.main.fragment_category_search.*
import okhttp3.*
import java.io.IOException

class RecipeDetailActivityFirebase : AppCompatActivity(){
    var recipeObj = Recipe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail_tab_layout)

        val sectionsPagerAdapterFirebase = SectionsPagerAdapterFirebase(this, supportFragmentManager)
//        recipe_view_pager.adapter = sectionsPagerAdapterFirebase
//        recipe_tab.setupWithViewPager(recipe_view_pager)

        supportActionBar?.elevation = 0f

//        println(urlComplete)

        getRecipeInfo()
    }

    private fun getRecipeInfo(){
        val recipeId = intent.getStringExtra(MainAdapter.CustomViewHolder.RECIPE_ID_KEY)
        val databaseRef = FirebaseDatabase.getInstance().reference
        val resultRef = databaseRef.child("Recipe")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {

                    var recID = ds.child("recipeID").getValue().toString()
                    var recName = ds.child("name").getValue().toString()
                    var recUser = ds.child("user").getValue().toString()
                    var recUrlPhoto: String =
                        ds.child("photo").getValue().toString()
                    var recipe =
                        Recipe(recID, recName, recUser, recUrlPhoto)
                    if(recipeId.equals(recID)){
                        recipeObj = recipe
                    }
                }
                tv_recipe_detail_profile_name.text =recipeObj.user
                tv_recipe_title.text = recipeObj.name
                Picasso.get().load(recipeObj.photo).into(recipe_image_detail)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(
                    "Error",
                    databaseError.getMessage()
                ) //Don't ignore errors!
            }
        }
        resultRef.addListenerForSingleValueEvent(eventListener)

    }
}

