package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Brand {
    @SerializedName("idbrand")
    @Expose
    var idbrand: Int =0;
    @SerializedName("name_brand")
    @Expose
    var name_brand: String = "";

    @SerializedName("state")
    @Expose
    var state: Int = 0

    constructor(){

    }

    constructor(idbrand: Int, name_brand: String, state: Int) {
        this.idbrand = idbrand
        this.name_brand = name_brand
        this.state = state
    }
}