package com.example.temptationmovile.clases


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Color {

    @SerializedName("idcolor")
    @Expose
    var idcolor: Int = 0;
    @SerializedName("name_col")
    @Expose
    var name_col: String = "";
    @SerializedName("state")
    @Expose
    var state:Int = 0
    constructor(){

    }

    constructor(idcolor: Int, name_col: String, state: Int) {
        this.idcolor = idcolor
        this.name_col = name_col
        this.state = state
    }
}