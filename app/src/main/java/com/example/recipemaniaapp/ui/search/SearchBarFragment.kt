package com.example.recipemaniaapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_category_search.*


class SearchBarFragment : Fragment(), View.OnClickListener {

    private val list = ArrayList<Recipe>()


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

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView = view.findViewById(R.id.searchView)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView . setOnQueryTextListener ( object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                val databaseRef = FirebaseDatabase.getInstance().reference
                val searchRef = databaseRef.child("Recipe").orderByChild("name")/*.equalTo(query)*/

                val querySize = query.length
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        list.clear()
                        Log.d("List Size", list.size.toString())

                        for (ds in dataSnapshot.children) {
                            var name = ds.child("name").getValue().toString()
                            val nameCut = name.toLowerCase().substring(0, querySize)
                            if (nameCut == query.toLowerCase()) {
                                Log.d("Result", name)
                                val resultRef =
                                    databaseRef.child("Recipe").orderByChild("name").equalTo(name)

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
                                            Log.d("URL", recUrlPhoto)
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


                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("Error", databaseError.getMessage()) //Don't ignore errors!
                    }
                }


                if(list.size > 0 ) {
                    Log.d("List Size Updated", list.size.toString())
                    rv_recipe_result.layoutManager = LinearLayoutManager(activity)
                    val listRecipeAdapter = CardViewRecipeAdapter(list)
                    rv_recipe_result.adapter = listRecipeAdapter
                    category_layout.visibility = View.INVISIBLE
                    rv_recipe_result.visibility = View.VISIBLE

                } else {
                    category_layout.visibility = View.INVISIBLE
                    rv_recipe_result.visibility = View.INVISIBLE
                    layout_not_found.visibility = View.VISIBLE
                }

                searchRef.addListenerForSingleValueEvent(valueEventListener)
                return true

            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    override fun onClick(v: View) {
    }
}




