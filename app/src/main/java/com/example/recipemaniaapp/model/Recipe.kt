package com.example.recipemaniaapp.model

class Recipe {

    var name: String? = null
    var information: String? = null
    var ingredient: String? = null
    var steps: String? = null
    var category: String? = null
    var like: Int = 0
    var user: String? = null
    var createdAt: String? = null

    constructor() {

    }

    constructor(name: String?, information: String?, ingredient: String?,
                steps:String?, category: String?, user:String?, createdAt: String?) {
        this.name = name
        this.information = information
        this.ingredient = ingredient
        this.steps = steps
        this.category = category
        this.user = user
        this.createdAt = createdAt
    }

}