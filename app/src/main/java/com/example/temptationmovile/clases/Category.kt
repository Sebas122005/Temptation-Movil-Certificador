package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @SerializedName("idcat")
    @Expose
    var idcat: Int = 0;
    @SerializedName("name_cat")
    @Expose
    var name_cat: String = "";
    @SerializedName("state")
    @Expose
    var state:Int = 0

    constructor(){

    }

    constructor(idcat: Int, name_cat: String, state: Int) {
        this.idcat = idcat
        this.name_cat = name_cat
        this.state = state
    }

}