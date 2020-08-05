package com.rhymartmanchus.yelpassignment.data.models

import com.google.gson.annotations.SerializedName

data class CategoryRaw (

    @SerializedName("alias")
    val alias: String,

    @SerializedName("title")
    val title: String

)