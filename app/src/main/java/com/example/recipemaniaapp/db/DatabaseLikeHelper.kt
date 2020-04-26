package com.example.recipemaniaapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.recipemaniaapp.db.DatabaseLikeContract.RecipeLikeColumns
import com.example.recipemaniaapp.db.DatabaseLikeContract.RecipeLikeColumns.Companion.TABLE_NAME

internal class DatabaseLikeHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbrecipeapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${RecipeLikeColumns.RECIPE_ID} TEXT NOT NULL, " +
                " ${RecipeLikeColumns.USER_EMAIL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}