package com.aliakberaakash.cutiehacksproject2020.data.model

data class Post (
    var id : String,
    var user : User,
    var description : String = "",
    var image : String,
    var likes : Map<User, Boolean> = mutableMapOf()
)