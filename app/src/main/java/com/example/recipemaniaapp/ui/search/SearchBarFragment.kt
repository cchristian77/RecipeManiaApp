package com.example.recipemaniaapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.recipemaniaapp.R


class SearchBarFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_bar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mCatergorySearchFragment =
            CatergorySearchFragment()
        val mFragmentManager = fragmentManager as FragmentManager
        mFragmentManager
            .beginTransaction()
            .add(
                R.id.content_fragment, mCatergorySearchFragment,
                CatergorySearchFragment::class.java.simpleName)
            .commit()

    }

    override fun onClick(v: View) {
    }
}




