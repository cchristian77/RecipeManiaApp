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
import kotlinx.android.synthetic.main.fragment_steps.*

/**
 * A simple [Fragment] subclass.
 */
class StepsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_steps.layoutManager = LinearLayoutManager(activity!!)
        recyclerView_steps.adapter = StepsAdapter()
    }

    private class StepsAdapter: RecyclerView.Adapter<StepsViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.recipe_detail_row, parent, false)

            return StepsViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {

        }

    }

    private class StepsViewHolder(val customView: View) : RecyclerView.ViewHolder(customView){

    }
}
