package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailIncome {

    @SerializedName("iddetincome")
    @Expose
    var iddetincome: Int = 0;

    @SerializedName("idincome")
    @Expose
    var idincome: Int = 0;

    @SerializedName("idproduc")
    @Expose
    var idproduc: Int = 0;

    @SerializedName("price_buy")
    @Expose
    var price_buy: Double = 0.0;

    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0;

    @SerializedName("igv")
    @Expose
    var igv: Double = 0.0;

    constructor()

    constructor(
                iddetincome: Int,
                idincome: Int,
                idproduc: Int,
                price_buy: Double,
                quantity: Int,
                igv: Double)
    {
        this.iddetincome=iddetincome
        this.idincome=idincome
        this.idproduc=idproduc
        this.price_buy=price_buy
        this.quantity=quantity
        this.igv=igv
    }
}