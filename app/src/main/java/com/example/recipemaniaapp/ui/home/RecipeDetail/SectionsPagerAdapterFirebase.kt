package com.example.recipemaniaapp.ui.home.RecipeDetail

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.example.recipemaniaapp.R

class SectionsPagerAdapterFirebase (private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.recipe_tab_text_1, R.string.recipe_tab_text_2, R.string.recipe_tab_text_3)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = InformationFragmentFirebase()
            1 -> fragment = IngredientsFragmentFirebase()
            2 -> fragment = StepsFragmentFirebase()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 3
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

}