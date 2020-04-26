package com.example.recipemaniaapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.recipemaniaapp.db.DatabaseLikeContract.RecipeLikeColumns.Companion.TABLE_NAME
import com.example.recipemaniaapp.db.DatabaseLikeContract.RecipeLikeColumns.Companion.USER_EMAIL
import com.example.recipemaniaapp.db.DatabaseLikeContract.RecipeLikeColumns.Companion.RECIPE_ID
import java.sql.SQLException

class LikeHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseLikeHelper: DatabaseLikeHelper
        private var INSTANCE: LikeHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): LikeHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LikeHelper(context)
            }

    }



    @Throws(SQLException::class)
    fun open() {
        database = dataBaseLikeHelper.writableDatabase
    }

    fun close() {
        dataBaseLikeHelper.close()
        if (database.isOpen) database.close()
    }

    init {
        dataBaseLikeHelper =  DatabaseLikeHelper(context)
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$RECIPE_ID ASC")
    }

    fun queryByUserEmail(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USER_EMAIL = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun queryByRecipeId(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$RECIPE_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun queryByEmailAndRecipeId(recipeId: String, email: String): Cursor {
        return database.rawQuery("SELECT * FROM $TABLE_NAME " +
                "WHERE $USER_EMAIL = '$email' AND $RECIPE_ID = '$recipeId'", null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateByUserEmail(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USER_EMAIL = ?", arrayOf(id))
    }

    fun delete(email: String, recipeId: String): Int {
        return database.delete(DATABASE_TABLE, "$USER_EMAIL = '$email'"
                + " AND $RECIPE_ID = '$recipeId'" , null)
    }

}