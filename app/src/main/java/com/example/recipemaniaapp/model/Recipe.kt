package com.example.recipemaniaapp.model

class Recipe {

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

    constructor(name: String?, information: String?, ingredient: String?,
                steps:String?, category: String?, user:String?, photo:String?, createdAt: String?) {
        this.name = name
        this.information = information
        this.ingredient = ingredient
        this.steps = steps
        this.category = category
        this.user = user
        this.photo = photo
        this.createdAt = createdAt
    }

    constructor(name: String?, information: String?, ingredient: String?,
                steps:String?, category: String?, like: Int?, user:String?, photo:String?, createdAt: String?) {
        this.name = name
        this.information = information
        this.ingredient = ingredient
        this.steps = steps
        this.category = category
        this.like = like
        this.user = user
        this.photo = photo
        this.createdAt = createdAt
    }

    constructor(name: String?, like: Int?, photo:String?) {
        this.name = name
        this.like = like
        this.photo = photo
    }

}