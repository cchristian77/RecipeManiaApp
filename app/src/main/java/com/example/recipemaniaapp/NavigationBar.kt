package com.example.recipemaniaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipemaniaapp.ui.newrecipe.AddFormFragment
import com.example.recipemaniaapp.ui.newrecipe.NewRecipeFragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth

class NavigationBar : AppCompatActivity() {

    lateinit var signedAccount: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        signedAccount = intent.getParcelableExtra("Google_Account")
        Log.d("Msg", signedAccount.email)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add_new_recipe,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var mFragmentManager = supportFragmentManager
        val fragment = mFragmentManager.findFragmentByTag(NewRecipeFragment::class.java.simpleName)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            when (item.itemId) {
                R.id.menu_sign_out -> {
                    signOut()
                    finish()
                    return true
                }
                else -> return true
            }
        }
        return true
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

}




