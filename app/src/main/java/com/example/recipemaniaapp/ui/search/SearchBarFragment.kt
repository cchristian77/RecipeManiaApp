package com.example.recipemaniaapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.search.adapter.CardViewRecipeAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_category_search.*


class SearchBarFragment : Fragment(), View.OnClickListener {

    private val list = ArrayList<Recipe>()
    lateinit var databaseRef : DatabaseReference
    lateinit var searchRef : Query


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mCategorySearchFragment = CatergorySearchFragment()
        val mFragmentManager = activity!!.supportFragmentManager
        mFragmentManager
            .beginTransaction()
            .add(
                R.id.content_fragment, mCategorySearchFragment,
                CatergorySearchFragment::class.java.simpleName)
            .commit()

        databaseRef = FirebaseDatabase.getInstance().reference
        searchRef = databaseRef.child("Recipe").orderByChild("name")

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView = view.findViewById(R.id.searchView)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.setOnQueryTextListener ( object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                val querySize = query.length
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        list.clear()

                        for (ds in dataSnapshot.children) {
                            var name = ds.child("name").getValue().toString()
                            var nameCut = name
                            if(name.length >= querySize) {
                                nameCut = name.toLowerCase().substring(0, querySize)
                            }
                            if (nameCut == query.toLowerCase()) {
                                Log.d("Result", name)
                                getRecipeData(name)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("Error", databaseError.getMessage()) //Don't ignore errors!
                    }

                }

                if(list.size > 0) {
                    Log.d("List Size", list.size.toString())
                    rv_recipe_result.layoutManager = LinearLayoutManager(activity!!)

                    val listRecipeAdapter = CardViewRecipeAdapter(list)
                    rv_recipe_result.adapter = listRecipeAdapter
                    category_layout.visibility = View.GONE
                    layout_not_found.visibility = View.GONE

                    tv_search_result.text = "\"" + query + "\" Search Result .";
                    result_layout.visibility = View.VISIBLE
                    tv_search_result.visibility = View.VISIBLE
                    rv_recipe_result.visibility = View.VISIBLE



                } else {
                    category_layout.visibility = View.GONE
                    rv_recipe_result.visibility = View.GONE
                    tv_no_result.text = "There's no recipe found."
                    layout_not_found.visibility = View.VISIBLE
                }

                searchRef.addListenerForSingleValueEvent(valueEventListener)
                searchView.clearFocus()
                return true

            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })



    }

    override fun onClick(v: View) {

    }

    private fun getRecipeData(name: String) {
        val resultRef =
            databaseRef.child("Recipe").orderByChild("name")
                .equalTo(name)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    var recName = ds.child("name").getValue().toString()
                    var recLike: String =
                        ds.child("like").getValue().toString()
                    var recUrlPhoto: String =
                        ds.child("photo").getValue().toString()
                    var recipe =
                        Recipe(recName, recLike.toInt(), recUrlPhoto)
                    list.add(recipe)
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
}




