package com.example.recipemaniaapp.ui.home.RecipeDetail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.ui.home.HomeFeed
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_information.*
import okhttp3.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class InformationFragment : Fragment() {
    var recipeId: Int? = null
    companion object{
        var RECIPE_ID = "RecipeId"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState != null){
            val recipeIdFromBundle = savedInstanceState.getInt(RECIPE_ID)
            recipeId = recipeIdFromBundle
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView_information.layoutManager = LinearLayoutManager(activity!!)
        getInformation()
        recyclerView_information.adapter = InformationAdapter()
    }

    private fun getInformation(){


        val recipeDetailUrl = "https://api.spoonacular.com/recipes/"
        val apiKey = "apiKey=5a1eeded0e2f478a9c751e8d68ecbe43"
        val addition = "&includeNutrition=true"

//        val urlComplete = recipeDetailUrl + recipeId + "/information?" + apiKey + addition

//        val request = Request.Builder().url(urlComplete).build()
//
//        val client = OkHttpClient()
//
//        client.newCall(request).enqueue(object: Callback{
//            override fun onResponse(call: Call, response: Response) {
//                val body = response?.body?.string()
////                println(body)
//
//                val gson = GsonBuilder().create()
//
//                val information = gson.fromJson(body, Information::class.java)
//
////                activity!!.runOnUiThread {
////                    recyclerView_home.adapter = MainAdapter(homeFeed)
////                }
//
//
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                println("Failed to execute request")
//            }
//        })
    }
    private class InformationAdapter: RecyclerView.Adapter<InformationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.recipe_detail_row, parent, false)

            return InformationViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {

        }

    }

    private class InformationViewHolder(val customView: View) : RecyclerView.ViewHolder(customView){

    }

}

class Information(val nutrition: List<Nutritions>)

class Nutritions (val nutrients: List<Nutrient>, val ingredients: List<Ingredients>, val caloricBreakdown: List<Nutrition>, val weightPerServing: List<WeightPerServing>)

class WeightPerServing (val amoung: Int, val unit: String)

class Ingredients(val name: String, val amount: Double, val unit: String)

class Nutrient(val title: String, val amount: Double, val unit: String, val percentOfDailyNeeds: Double)

class Nutrition (val percentProtein: Double, val percentFat: Double, val percentCarbs: Double)
