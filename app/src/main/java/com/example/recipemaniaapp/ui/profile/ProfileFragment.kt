//import android.os.Bundle
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.snackbar.Snackbar
//import com.google.android.material.tabs.TabLayout
//import androidx.viewpager.widget.ViewPager
//import androidx.appcompat.app.AppCompatActivity
//import android.view.Menu
//import android.view.MenuItem
//import com.example.recipemaniaapp.R
//import com.example.recipemaniaapp.ui.profile.SectionsPagerAdapter
//
//class ProfileFragment : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_profile)
//
//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        val viewPager: ViewPager = findViewById(R.id.profile_viewPager)
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = findViewById(R.id.profile_tabs)
//        tabs.setupWithViewPager(viewPager)
//    }
//}

package com.example.recipemaniaapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.recipemaniaapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        if(acct != null){
            val personName = acct.displayName
            val personPhoto = acct.photoUrl

            tv_profile_name.text = personName
            Picasso.get().load(personPhoto).into(img_profile)
        }
    }
    
//    private fun setupViewPager(viewPager : ViewPager){
//        var adapter = SectionsPagerAdapter(getSup)
//
//    }
}