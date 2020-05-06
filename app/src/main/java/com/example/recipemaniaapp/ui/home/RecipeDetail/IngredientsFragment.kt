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
import kotlinx.android.synthetic.main.fragment_information.*
import kotlinx.android.synthetic.main.fragment_ingredients.*

/**
 * A simple [Fragment] subclass.
 */
class IngredientsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_ingredients.layoutManager = LinearLayoutManager(activity!!)
        recyclerView_ingredients.adapter = IngredientsAdapter()
    }

    private class IngredientsAdapter: RecyclerView.Adapter<IngredientsViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val customView = layoutInflater.inflate(R.layout.recipe_detail_row, parent, false)

            return IngredientsViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {

        }

    }

    private class IngredientsViewHolder(val customView: View) : RecyclerView.ViewHolder(customView){

    }

}
