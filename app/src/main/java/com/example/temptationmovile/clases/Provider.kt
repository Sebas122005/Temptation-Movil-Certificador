package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Provider {

    @SerializedName("idprovider")
    @Expose
    var idprovider: Int =0;

    @SerializedName("name_prov")
    @Expose
    var name_prov: String = "";

    @SerializedName("ruc")
    @Expose
    var ruc: String = "";

    @SerializedName("company_name")
    @Expose
    var company_name: String = "";

    @SerializedName("phone")
    @Expose
    var phone: Int = 0;

    @SerializedName("email")
    @Expose
    var email: String = "";

    @SerializedName("description")
    @Expose
    var description: String = "";

    @SerializedName("address")
    @Expose
    var address: String = "";

    @SerializedName("state")
    @Expose
    var state: Int = 0



    constructor(
        idprovider: Int,
        name_prov: String,
        ruc: String,
        company_name: String,
        phone: Int,
        email: String,
        description: String,
        address: String,
        state: Int
    ) {
        this.idprovider = idprovider
        this.name_prov = name_prov
        this.ruc = ruc
        this.company_name = company_name
        this.phone = phone
        this.email = email
        this.description = description
        this.address = address
        this.state = state
    }

    constructor()


}