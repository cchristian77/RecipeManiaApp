package com.example.recipemaniaapp.ui.search.adapter

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.recipemaniaapp.MainActivity
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.db.DatabaseLikeContract
import com.example.recipemaniaapp.db.LikeHelper
import com.example.recipemaniaapp.helper.MappingLikeHelper
import com.example.recipemaniaapp.model.Like
import com.example.recipemaniaapp.model.Recipe
import com.example.recipemaniaapp.ui.home.RecipeDetail.RecipeDetailActivity
import com.example.recipemaniaapp.ui.home.RecipeDetail.RecipeDetailActivityFirebase
import com.example.recipemaniaapp.ui.home.adapter.MainAdapter.CustomViewHolder.Companion.RECIPE_ID_KEY
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_cardview_recipe.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CardViewRecipeAdapter(private val listRecipe: ArrayList<Recipe>, private val activity: Activity) :
    RecyclerView.Adapter<CardViewRecipeAdapter.CardViewViewHolder>() {

    private lateinit var likeHelper: LikeHelper
    lateinit var likeBtn : ImageButton

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_cardview_recipe, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int =  listRecipe.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe) {
            with(itemView) {
                likeBtn = findViewById(R.id.like_button)
                likeHelper = LikeHelper.getInstance(activity.applicationContext)
                likeHelper.open()
                val user = GoogleSignIn.getLastSignedInAccount(activity)
                val user_email = user?.email.toString()
                loadLikeAsync(user_email, recipe.recipeID.toString())

                val checkUrl : Boolean = URLUtil.isValidUrl(recipe.photo);

                if(checkUrl) {
                    val into = Glide.with(itemView.context)
                        .load(recipe.photo)
                        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(35)))
                        .into(recipe_image)
                }

                tv_recipe_name.text = recipe.name + " Recipe"
                tv_like_count.text = recipe.like.toString() + " Likes"

                val databaseRef = FirebaseDatabase.getInstance().reference

                like_button.setOnClickListener {
                    var temp = tv_like_count.text.toString()
                    var arr = temp.split(" ")
                    var like = arr[0].toInt()
                    var heartPink = resources.getDrawable(R.drawable.icon_heart)
                    if (like_button.drawable.constantState == heartPink.constantState)  {
                        val values = ContentValues()
                        values.put(
                            DatabaseLikeContract.RecipeLikeColumns.RECIPE_ID,
                            recipe.recipeID
                        )
                        values.put(
                            DatabaseLikeContract.RecipeLikeColumns.USER_EMAIL,
                            user_email
                        )
                        val result = likeHelper.insert(values)

                        if (result > 0) {
                            like+=1
                            val updateRef = databaseRef.child("Recipe")
                                .child(recipe.recipeID.toString())
                                .child("like")
                                .setValue(like)
                            like_button.setImageResource(R.drawable.icon_heartpink)
                            tv_like_count.text = "$like Likes"
                        } else {
                            Toast.makeText(activity, "Failed to like.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        val result = likeHelper.delete(user_email, recipe.recipeID.toString())

                        if (result > 0) {
                            like-=1
                            val updateRef = databaseRef.child("Recipe")
                                .child(recipe.recipeID.toString())
                                .child("like")
                                .setValue(like)
                            like_button.setImageResource(R.drawable.icon_heart)
                            tv_like_count.text = "$like Likes"
                        } else {
                            Toast.makeText(activity, "Failed to unlike.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                save_btn.setOnClickListener{
                    Toast.makeText(itemView.context, "Save ${recipe.name}", Toast.LENGTH_SHORT).show() }

                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "Show ${recipe.name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(itemView.context, RecipeDetailActivityFirebase::class.java)
                    intent.putExtra(RECIPE_ID_KEY, recipe.recipeID.toString())
                    itemView.context.startActivity(intent)
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listRecipe[position])
    }

    private fun loadLikeAsync(email : String, recipeID: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredLikes = async(Dispatchers.IO) {
                val cursor = likeHelper.queryByEmailAndRecipeId(recipeID, email)
                MappingLikeHelper.mapCursorToArrayList(cursor)
            }
            val like = deferredLikes.await()
            var size = like.size
            if(size > 0) {
                Log.d("Like", like.size.toString())
                likeBtn.setImageResource(R.drawable.icon_heartpink)
            } else {
                likeBtn.setImageResource(R.drawable.icon_heart)
            }

            like.clear()
        }


    }




}