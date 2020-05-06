package com.example.recipemaniaapp.ui.home.RecipeDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.ui.home.RecipeDetailAppBar
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_detail_tab_layout.*
import okhttp3.*
import java.io.IOException

class RecipeDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail_tab_layout)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        recipe_view_pager.adapter = sectionsPagerAdapter
        recipe_tab.setupWithViewPager(recipe_view_pager)

        supportActionBar?.elevation = 0f

//        println(urlComplete)

        getRecipeInfo()
    }

    private fun getRecipeInfo(){
        val recipeId = intent.getIntExtra(MainAdapter.CustomViewHolder.RECIPE_ID_KEY, -1)
        val recipeDetailUrl = "https://api.spoonacular.com/recipes/"
        val apiKey = "apiKey=5a1eeded0e2f478a9c751e8d68ecbe43"
        val addition = "&includeNutrition=true"

        val urlComplete = recipeDetailUrl + recipeId + "/information?" + apiKey + addition

        val client = OkHttpClient()
        val request = Request.Builder().url(urlComplete).build()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                val gson =GsonBuilder().create()
                val detailFeed = gson.fromJson(body, RecipeDetailAppBar::class.java)

                runOnUiThread {
                    //show data
                    Picasso.get().load(detailFeed.image).into(img_profile)//profile pict

                    val profileName: String = detailFeed.sourceName.toString()//profileName
                    tv_recipe_detail_profile_name.text = profileName

                    val recipeTitle : String = detailFeed.title.toString()//recipe title
                    tv_recipe_title.text = recipeTitle

                    Picasso.get().load(detailFeed.image).into(recipe_image_detail)//recipe Photo

                    val recipeId: String = detailFeed.id.toString()

                }
//                println(body)
            }

        })
    }
}

