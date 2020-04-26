package com.example.recipemaniaapp.helper

import android.database.Cursor
import android.provider.Settings.Global.getString
import com.example.recipemaniaapp.model.Like
import com.example.recipemaniaapp.db.DatabaseLikeContract
import java.lang.reflect.Array.getInt

object MappingLikeHelper {
    fun mapCursorToArrayList(likesCursor: Cursor?): ArrayList<Like> {
        val likeList = ArrayList<Like>()

        likesCursor?.apply {
            while (moveToNext()) {
                val email  = getString(getColumnIndexOrThrow(DatabaseLikeContract.RecipeLikeColumns.USER_EMAIL))
                val recipeID = getString(getColumnIndexOrThrow(DatabaseLikeContract.RecipeLikeColumns.RECIPE_ID))
                likeList.add(Like(recipeID, email))
            }
        }

        return likeList
    }
}