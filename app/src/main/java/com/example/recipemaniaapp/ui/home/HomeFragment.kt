package com.example.recipemaniaapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.home.RecipeDetail.InformationFragment
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter
import com.example.recipemaniaapp.ui.search.adapter.CardViewRecipeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_category_search.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException


class HomeFragment : Fragment(){
    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
    private val list = ArrayList<Recipe>()

//    private val list = ArrayList<Recipes>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_home.layoutManager = LinearLayoutManager(activity!!)
//        recyclerView_home.adapter = MainAdapter()
        getDataFromFirebase()
        getRecipes()
    }


    private fun getDataFromFirebase(){
        val databaseRef = FirebaseDatabase.getInstance().reference
        val resultRef = databaseRef.child("Recipe")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (ds in dataSnapshot.children) {
                    var recID = ds.child("recipeID").getValue().toString()
                    var recName = ds.child("name").getValue().toString()
                    var recLike: String =
                        ds.child("like").getValue().toString()
                    var recUrlPhoto: String =
                        ds.child("photo").getValue().toString()
                    var recipe =
                        Recipe(recID, recName, recLike.toInt(), recUrlPhoto)
                    list.add(recipe)
                }

                if(list.size > 0 ) {
                    rv_recipe_home.layoutManager = LinearLayoutManager(activity!!)
                    val listRecipeAdapter = CardViewRecipeAdapter(list, activity!!)
                    rv_recipe_home.adapter = listRecipeAdapter
                }
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
    private fun getRecipes() {
        println("Attempting to Fetch JSON")
        val url = "https://api.spoonacular.com/recipes/search?apiKey="
        val key = "5a1eeded0e2f478a9c751e8d68ecbe43"
        val numofRecipes = 15
        val urlComplete = url + key + "&number=" + numofRecipes

        val request = Request.Builder().url(urlComplete).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                activity!!.runOnUiThread {
                    recyclerView_home.adapter = MainAdapter(homeFeed)
                }


            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}

class HomeFeed(val results: List<Recipes>)//nama val results harus sama dengan nama di JSON root (disini namanya results)

class Recipes(val id:Int, val title: String, val readyInMinutes: String, val servings: Int, val sourceUrl: String, val image: String)

class RecipeDetailAppBar(val id:Int, val title: String, val sourceName: String, val image: String)