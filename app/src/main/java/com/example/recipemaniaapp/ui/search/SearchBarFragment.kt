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
import com.example.recipemaniaapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchBarFragment : Fragment(), View.OnClickListener {
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
                Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
                val databaseRef = FirebaseDatabase.getInstance().reference
                val searchRef = databaseRef.child("Recipe").orderByChild("name")/*.equalTo(query)*/

                val querySize = query.length
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds  in dataSnapshot.children) {
                            var name = ds.child("name").getValue().toString()
                            val nameCut = name.toLowerCase().substring(0, querySize)
                            if(nameCut == query.toLowerCase())
                                Log.d("Search Result", name)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("Error", databaseError.getMessage()) //Don't ignore errors!
                    }
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




