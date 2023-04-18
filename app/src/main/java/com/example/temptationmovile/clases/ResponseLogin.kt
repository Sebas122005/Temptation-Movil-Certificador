package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin {
    @SerializedName("username")
    @Expose
    var username: String = "";
    @SerializedName("password")
    @Expose
    var password: String = "";
    constructor(username: String) {
        this.username = username
    }

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    constructor()
}