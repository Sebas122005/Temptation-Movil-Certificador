package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rol {
    @SerializedName("idrol")
    @Expose
    var idrol:Int=0
    @SerializedName("namerol")
    @Expose
    var namerol:String=""
    @SerializedName("state")
    @Expose
    var state:Int=0

    constructor(){}
    constructor(idrol: Int, namerol: String, state: Int) {
        this.idrol = idrol
        this.namerol = namerol
        this.state = state
    }
}