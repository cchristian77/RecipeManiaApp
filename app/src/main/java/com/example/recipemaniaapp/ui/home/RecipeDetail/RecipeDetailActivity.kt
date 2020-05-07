package com.example.recipemaniaapp.ui.home.RecipeDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.ui.home.*
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_detail_tab_layout.*
import kotlinx.android.synthetic.main.activity_recipe_detail_tab_layout.recyclerView_steps
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_steps.*
import kotlinx.android.synthetic.main.recipe_detail_row.view.*
import kotlinx.android.synthetic.main.recipe_step_row.view.*
import okhttp3.*
import java.io.IOException

class RecipeDetailActivity : AppCompatActivity(){
    var infoFrag = InformationFragment()
    var bundleInfo = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail_tab_layout)

        recyclerView_information.layoutManager = LinearLayoutManager(this)
        recyclerView_ingredients.layoutManager = LinearLayoutManager(this)
        recyclerView_steps.layoutManager = LinearLayoutManager(this)

//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        getRecipeInfo()
//        recipe_view_pager.adapter = sectionsPagerAdapter
//        recipe_tab.setupWithViewPager(recipe_view_pager)

        supportActionBar?.elevation = 0f

//        println(urlComplete)
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

                    recyclerView_information.adapter = InformationAdapter(detailFeed.nutrition)
                    ViewCompat.setNestedScrollingEnabled(recyclerView_information, false)

                    recyclerView_ingredients.adapter = IngredientAdapter(detailFeed.nutrition)
                    ViewCompat.setNestedScrollingEnabled(recyclerView_ingredients, false)

                    recyclerView_steps.adapter = StepAdapter(detailFeed.analyzedInstructions)
                }
            }

        })
    }
    private class InformationAdapter(val nutrition: Nutrition): RecyclerView.Adapter<InformationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.recipe_detail_row, parent, false)

            return InformationViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return nutrition.nutrients.count()
        }

        override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
            val nutrients = nutrition.nutrients.get(position)
            val title = nutrients.title
            val amount = nutrients.amount
            val unit = nutrients.unit
            val complete = title + " : " + amount + " " + unit
            holder.customView.tv_recipe_detail.text = complete


        }

    }

    private class InformationViewHolder(val customView: View, var nutritient: Nutrients? = null) : RecyclerView.ViewHolder(customView){

    }
    private class IngredientAdapter(val nutrition: Nutrition): RecyclerView.Adapter<IngredientsViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.recipe_detail_row, parent, false)

            return IngredientsViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return nutrition.ingredients.count()
        }

        override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
            val ingredients = nutrition.ingredients.get(position)
            val name = ingredients.name
            val amount = ingredients.amount
            val unit = ingredients.unit
            val complete = name + " : " +  amount +" " + unit
            holder?.customView.tv_recipe_detail.text = complete
        }

    }
    private class IngredientsViewHolder(val customView: View, var ingredient: Ingredients? = null) : RecyclerView.ViewHolder(customView)

    private class StepAdapter(val analyzedInstruction: List<AnalyzedInstruction>): RecyclerView.Adapter<StepsViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.recipe_step_row, parent, false)

            return StepsViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return analyzedInstruction[0].steps.count()
        }

        override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
            val steps = analyzedInstruction[0].steps.get(position)
            holder?.customView.tv_recipe_step_number.text = steps.number.toString()
            holder?.customView.tv_recipe_step_detail.text = steps.step.toString()
        }

    }
    private class StepsViewHolder(val customView: View, var step: Step? = null) : RecyclerView.ViewHolder(customView)
}

