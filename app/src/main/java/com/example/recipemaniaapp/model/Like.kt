package com.example.recipemaniaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Like (
    var recipe_id: String? = null,
    var user_email: String? = null

) : Parcelable