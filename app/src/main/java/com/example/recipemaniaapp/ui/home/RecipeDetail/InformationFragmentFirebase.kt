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
class InformationFragmentFirebase : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getInformation(){

    }

}
