package com.example.recipemaniaapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.recipemaniaapp.R
import kotlinx.android.synthetic.main.fragment_category_search.*

class CatergorySearchFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_not_found.visibility = View.INVISIBLE
        rv_recipe_result.visibility = View.INVISIBLE

    }

    override fun onClick(v: View) {

    }
}