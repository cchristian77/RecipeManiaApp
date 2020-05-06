package com.example.recipemaniaapp.model

class Recipe {
    var recipeID : String? = null
    var name: String? = null
    var information: String? = null
    var ingredient: String? = null
    var steps: String? = null
    var category: String? = null
    var like: Int? = 0
    var user: String? = null
    var photo: String? = null
    var createdAt: String? = null

    constructor() {

    }

    constructor(recipeID: String?, name: String?, information: String?, ingredient: String?,
                steps:String?, category: String?, user:String?, photo:String?, createdAt: String?) {
        this.recipeID = recipeID
        this.name = name
        this.information = information
        this.ingredient = ingredient
        this.steps = steps
        this.category = category
        this.user = user
        this.photo = photo
        this.createdAt = createdAt
    }

    constructor(recipeID: String?, name: String?, like: Int?, photo:String?) {
        this.recipeID = recipeID
        this.name = name
        this.like = like
        this.photo = photo
    }

    constructor(name:String?, photo:String?, recipeId:String?){
        this.recipeID = recipeId
        this.name = name
        this.photo = photo
    }
    constructor(name:String?, photo:String?, recipeId:String?, user: String?){
        this.recipeID = recipeId
        this.name = name
        this.photo = photo
        this.user = user
    }

}