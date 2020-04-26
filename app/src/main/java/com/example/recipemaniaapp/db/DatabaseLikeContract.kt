package com.example.recipemaniaapp.db
import android.provider.BaseColumns

internal class DatabaseLikeContract {
    internal class RecipeLikeColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "recipe_like"
            const val RECIPE_ID = "recipe_id"
            const val USER_EMAIL = "user_email"
        }
    }


}