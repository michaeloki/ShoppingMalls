package com.inspirati.shoppinglist.api

import com.inspirati.shoppinglist.model.*
import retrofit2.Call
import retrofit2.http.*

internal interface ApiInterface {

    @get:GET("/v2/5b7e8bc03000005c0084c210")
    val doGetCities: Call<ShoppingEntity>

}