package com.example.recipemaniaapp.ui.search.adapter

import android.app.Activity
import android.content.ContentValues
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.recipemaniaapp.MainActivity
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.db.DatabaseLikeContract
import com.example.recipemaniaapp.db.LikeHelper
import com.example.recipemaniaapp.helper.MappingLikeHelper
import com.example.recipemaniaapp.model.Recipe
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_cardview_recipe, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int =  listRecipe.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe) {
            with(itemView) {
                val into = Glide.with(itemView.context)
                    .load(recipe.photo)
                    .apply(RequestOptions().override(350, 550))
                    .into(recipe_image)

                likeHelper = LikeHelper.getInstance(activity.applicationContext)
                likeHelper.open()


                val user = GoogleSignIn.getLastSignedInAccount(activity)
                val user_email = user?.email.toString()

                tv_recipe_name.text = recipe.name + " Recipe"

                tv_like_count.text = recipe.like.toString() + " Likes"

                var isLike = loadLikeAsync(user_email, recipe.recipeID.toString())

                val databaseRef = FirebaseDatabase.getInstance().reference


                if(isLike) {
                    like_button.setImageResource(R.drawable.icon_heartpink)
                }

                like_button.setOnClickListener {
                    var temp = recipe.like.toString()
                    if (temp != null) {
                        if (!isLike) {

                            var like = temp.toInt()
                            var updateLike = like + 1
                            val updateRef = databaseRef.child("Recipe")
                                .child(recipe.recipeID.toString())
                                .child("like")
                                .setValue(like)
                            like_button.setImageResource(R.drawable.icon_heartpink)
                            tv_like_count.text = "$updateLike Likes"

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
                                Toast.makeText(activity, "Sukses menambah data", Toast.LENGTH_SHORT)
                                    .show()
                                isLike = true
                            } else {
                                Toast.makeText(activity, "Gagal menambah data", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {

                            var like = temp.toInt()
                            var updateLike = like - 1
                            val updateRef = databaseRef.child("Recipe")
                                .child(recipe.recipeID.toString())
                                .child("like")
                                .setValue(like)
                            like_button.setImageResource(R.drawable.icon_heart)
                            tv_like_count.text = "$updateLike Likes"

                            val values = ContentValues()
                            values.put(
                                DatabaseLikeContract.RecipeLikeColumns.RECIPE_ID,
                                recipe.recipeID
                            )
                            values.put(
                                DatabaseLikeContract.RecipeLikeColumns.USER_EMAIL,
                                user_email
                            )
                            val result = likeHelper.delete(user_email, recipe.recipeID.toString())

                            if (result > 0) {
                                Toast.makeText(activity, "Sukses hapus data", Toast.LENGTH_SHORT)
                                    .show()
                                isLike = false
                            } else {
                                Toast.makeText(activity, "Gagal hapus data", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }




                }

                save_btn.setOnClickListener{
                    Toast.makeText(itemView.context, "Save ${recipe.name}", Toast.LENGTH_SHORT).show() }

                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "Show ${recipe.name}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listRecipe[position])
    }

    private fun loadLikeAsync(email : String, recipeID: String): Boolean {
        var isLike : Boolean = false
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = likeHelper.queryByEmailAndRecipeId(recipeID, email)
                MappingLikeHelper.mapCursorToArrayList(cursor)
            }
            val like = deferredNotes.await()
            if (like.size > 0) {
                isLike = true
            }
        }

        return isLike

    }




}