package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {
    @SerializedName("idproduc")
    @Expose
    var idproduc: Int = 0;

    @SerializedName("idcat")
    @Expose
    var idcat: Int = 0;

    @SerializedName("idsize")
    @Expose
    var idsize: Int = 0;

    @SerializedName("idstyles")
    @Expose
    var idstyles: Int = 0;

    @SerializedName("idbrand")
    @Expose
    var idbrand: Int = 0;

    @SerializedName("idcolor")
    @Expose
    var idcolor: Int = 0;

    @SerializedName("name_p")
    @Expose
    var name_p: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("price")
    @Expose
    var price: Double = 0.0

    @SerializedName("stock")
    @Expose
    var stock: Int = 0

    @SerializedName("image_front")
    @Expose
    var image_front: String = ""

    @SerializedName("image_back")
    @Expose
    var image_back: String = ""

    @SerializedName("image_using")
    @Expose
    var image_using: String = ""

    var state: Int = 0;

    constructor()


    constructor(
        idproduc: Int,
        idcat: Int,
        idsize: Int,
        idstyles: Int,
        idbrand: Int,
        idcolor: Int,
        name_p: String,
        description: String,
        price: Double,
        stock: Int,
        image_front: String,
        image_back: String,
        image_using: String,
        state: Int
    ) {
        this.idproduc = idproduc
        this.idcat = idcat
        this.idsize = idsize
        this.idstyles = idstyles
        this.idbrand = idbrand
        this.idcolor = idcolor
        this.name_p = name_p
        this.description = description
        this.price = price
        this.stock = stock
        this.image_front = image_front
        this.image_back = image_back
        this.image_using = image_using
        this.state = state
    }


}