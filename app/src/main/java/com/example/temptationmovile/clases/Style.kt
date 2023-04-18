package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Style {

    @SerializedName("idstyles")
    @Expose
    var idstyles: Int = 0;
    @SerializedName("name_sty")
    @Expose
    var name_sty: String = "";
    @SerializedName("state")
    @Expose
    var state:Int = 0
    constructor(){

    }

    constructor(idstyles: Int, name_sty: String, state: Int) {
        this.idstyles = idstyles
        this.name_sty = name_sty
        this.state = state
    }

}