package com.inspirati.shoppinglist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Mall {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("shops")
    @Expose
    var shops: List<Shop>? = null

}