package com.inspirati.shoppinglist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Shop {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}