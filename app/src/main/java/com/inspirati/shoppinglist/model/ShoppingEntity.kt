package com.inspirati.shoppinglist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShoppingEntity {

    @SerializedName("cities")
    @Expose
    var cities: List<City>? = null

}