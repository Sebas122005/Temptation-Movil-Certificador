package com.example.temptationmovile.clases

import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

class Person {

    @SerializedName("idperson")
    @Expose
    var idperson:Int=0
    @SerializedName("idrol")
    @Expose
    var idrol:Int=0
    @SerializedName("name")
    @Expose
    var name:String =""
    @SerializedName("lastname")
    @Expose
    var lastname:String=""
    @SerializedName("date_b")
    @Expose
    var date_b:String=""
    @SerializedName("dni")
    @Expose
    var dni:String=""
    @SerializedName("gender")
    @Expose
    var gender:String=""
    @SerializedName("address")
    @Expose
    var address:String=""
    @SerializedName("username")
    @Expose
    var username:String=""
    @SerializedName("password")
    @Expose
    var password:String=""
    @SerializedName("state")
    @Expose
    var state:Int=0
    @SerializedName("key")
    @Expose
    var key:String=""

constructor()
    constructor(
        idperson: Int,
        idrol: Int,
        name: String,
        lastname: String,
        date_b: String,
        dni: String,
        gender: String,
        address: String,
        username: String,
        password: String,
        state: Int,
        key: String
    ) {
        this.idperson = idperson
        this.idrol = idrol
        this.name = name
        this.lastname = lastname
        this.date_b = date_b
        this.dni = dni
        this.gender = gender
        this.address = address
        this.username = username
        this.password = password
        this.state = state
        this.key = key
    }
}