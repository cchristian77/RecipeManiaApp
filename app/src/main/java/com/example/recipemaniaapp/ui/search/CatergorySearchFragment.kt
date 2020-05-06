package com.example.recipemaniaapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.search.adapter.CardViewRecipeAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_category_search.*

class CatergorySearchFragment : Fragment(), View.OnClickListener {

    private val list = ArrayList<Recipe>()
    lateinit  var query : String
    lateinit var databaseRef : DatabaseReference
    lateinit var searchRef : Query
    private var listSize : Int = 0

    companion object {
        val EXTRA_QUERY = "QUERY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_not_found.visibility = View.GONE
        result_layout.visibility = View.GONE
        progressBar.visibility = View.GONE

        back_btn_result.setOnClickListener(this)
        back_btn_not_found.setOnClickListener(this)
        cv_pasta.setOnClickListener(this)
        cv_dessert.setOnClickListener(this)
        cv_meal.setOnClickListener(this)
        cv_pizza.setOnClickListener(this)

        databaseRef = FirebaseDatabase.getInstance().reference
        searchRef = databaseRef.child("Recipe").orderByChild("name")

        checker()
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.back_btn_result -> {
                result_layout.visibility = View.GONE
                layout_not_found.visibility = View.GONE
                category_layout.visibility = View.VISIBLE
            }
            R.id.back_btn_not_found -> {
                layout_not_found.visibility = View.GONE
                result_layout.visibility = View.GONE
                category_layout.visibility = View.VISIBLE
            }
            R.id.cv_pasta -> {
                searchByCategory("Pasta")
            }
            R.id.cv_dessert -> {
                searchByCategory("Dessert")
            }
            R.id.cv_meal -> {
                searchByCategory("Meal")
            }
            R.id.cv_pizza-> {
                searchByCategory("Pizza")
            }
        }

    }

    private fun searchByCategory(category: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference
        val resultRef =
            databaseRef.child("Recipe").orderByChild("category")
                .equalTo(category)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                progressBar.visibility = View.VISIBLE
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
                    progressBar.visibility = View.GONE
                    rv_recipe_result.layoutManager = LinearLayoutManager(activity!!)
                    val listRecipeAdapter = CardViewRecipeAdapter(list, activity!!)
                    rv_recipe_result.adapter = listRecipeAdapter
                    category_layout.visibility = View.GONE
                    layout_not_found.visibility = View.GONE

                    tv_search_result.text = category + " Category Result.";
                    result_layout.visibility = View.VISIBLE
                    tv_search_result.visibility = View.VISIBLE
                    rv_recipe_result.visibility = View.VISIBLE

                } else {
                    progressBar.visibility = View.GONE
                    category_layout.visibility = View.GONE
                    result_layout.visibility = View.GONE
                    tv_no_result.text = "There's no recipe found in " + category + " category."
                    layout_not_found.visibility = View.VISIBLE
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

    private fun checker() {
        if(arguments != null) {
            var temp = arguments?.getString(EXTRA_QUERY)
            if(temp != null) query = temp
            if(query != null) {
                Log.d("Query", query)
                getResult(query)
            }
        }
    }

    private fun getResult(query : String) {
        val databaseRef = FirebaseDatabase.getInstance().reference
        val searchRef = databaseRef.child("Recipe").orderByChild("name")
        val querySize = query.length
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (ds in dataSnapshot.children) {
                    var name = ds.child("name").getValue().toString()
                    var nameCut = name
                    if (name.length >= querySize) {
                        nameCut = name.toLowerCase().substring(0, querySize)
                    }
                    if (nameCut == query.toLowerCase()) {
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
                }

                if(list.size > 0 ) {
                    rv_recipe_result.layoutManager = LinearLayoutManager(activity!!)
                    val listRecipeAdapter = CardViewRecipeAdapter(list, activity!!)
                    rv_recipe_result.adapter = listRecipeAdapter
                    category_layout.visibility = View.GONE
                    layout_not_found.visibility = View.GONE

                    tv_search_result.text = "\"" + query + "\" Search Result .";
                    result_layout.visibility = View.VISIBLE
                    tv_search_result.visibility = View.VISIBLE
                    rv_recipe_result.visibility = View.VISIBLE

                } else {
                    category_layout.visibility = View.GONE
                    result_layout.visibility = View.GONE
                    tv_no_result.text = "There's no recipe is found."
                    layout_not_found.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", databaseError.getMessage()) //Don't ignore errors!
            }

        }

        searchRef.addListenerForSingleValueEvent(valueEventListener)
    }

}