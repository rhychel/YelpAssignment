package com.rhymartmanchus.yelpassignment.data.api.models

import com.google.gson.annotations.SerializedName

data class ReviewRaw (
    val userName: String,
    val userImageUrl: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("time_created")
    val timeCreated: String
)