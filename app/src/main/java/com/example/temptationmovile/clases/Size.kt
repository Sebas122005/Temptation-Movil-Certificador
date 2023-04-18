package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Size {
    @SerializedName("idsize")
    @Expose
    var idsize:Int=0
    @SerializedName("name_size")
    @Expose
    var name_size:String=""
    @SerializedName("state")
    @Expose
    var state:Int=0

    constructor(){}
    constructor(idsize: Int, name_size: String, state: Int) {
        this.idsize = idsize
        this.name_size = name_size
        this.state = state
    }
}