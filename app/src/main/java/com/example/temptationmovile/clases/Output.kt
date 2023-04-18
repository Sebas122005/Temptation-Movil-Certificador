package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Output {
    @SerializedName("idout")
    @Expose
    var idout: Int = 0;
    @SerializedName("idproduc")
    @Expose
    var idproduc: Int = 0;
    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0;
    @SerializedName("dateout")
    @Expose
    var dateout: String = "";
    @SerializedName("destino")
    @Expose
    var destino: String = "";
    @SerializedName("state")
    @Expose
    var state:Int = 0

    constructor(){

    }
    constructor(
        idout: Int,
        idproduc: Int,
        quantity: Int,
        dateout: String,
        destino: String,
        state: Int
    ) {
        this.idout = idout
        this.idproduc = idproduc
        this.quantity = quantity
        this.dateout = dateout
        this.destino = destino
        this.state = state
    }
}