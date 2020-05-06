package com.example.recipemaniaapp.ui.profile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


import com.example.recipemaniaapp.R

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    val TAG ="FragmentAdapter"
    lateinit var mFragmentList : ArrayList<Fragment>
    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    private fun addFragment(fragment: Fragment){
        mFragmentList.add(fragment)
    }
}