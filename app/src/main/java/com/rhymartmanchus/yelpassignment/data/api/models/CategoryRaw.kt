package com.rhymartmanchus.yelpassignment.data.api.models

import com.google.gson.annotations.SerializedName

data class CategoryRaw (

    @SerializedName("alias")
    val alias: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("parent_aliases")
    val parentAliases: List<String>

)