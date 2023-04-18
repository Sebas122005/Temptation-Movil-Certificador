package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Income {

    @SerializedName("idincome")
    @Expose
    var idincome: Int = 0;

    @SerializedName("idprovider")
    @Expose
    var idprovider: Int = 0;

    @SerializedName("dateinco")
    @Expose
    var dateinco: String = ""

    @SerializedName("state")
    @Expose
    var state: Int = 0;


    constructor()

    constructor(idincome: Int,
                idprovider: Int,
                dateinco: String,
                state: Int)
    {
        this.idincome=idincome
        this.idprovider=idprovider
        this.dateinco=dateinco
        this.state=state
    }

}