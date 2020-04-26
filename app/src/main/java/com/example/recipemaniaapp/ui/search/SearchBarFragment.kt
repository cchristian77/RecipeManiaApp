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
import com.example.recipemaniaapp.ui.newrecipe.AddFormFragment
import com.example.recipemaniaapp.ui.search.adapter.CardViewRecipeAdapter
import com.google.firebase.database.*
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
        searchView.setOnQueryTextListener ( object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("Query", query)
                val fm = activity!!.supportFragmentManager
                val mCatergorySearchFragment = CatergorySearchFragment()
                var mBundle = Bundle()
                mBundle.putString(CatergorySearchFragment.EXTRA_QUERY, query)
                fm
                    .beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(
                        R.id.content_fragment,
                        mCatergorySearchFragment,
                        AddFormFragment::class.java.simpleName
                    )
                    .commit()
                mCatergorySearchFragment.arguments = mBundle
//                searchView.clearFocus()
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




