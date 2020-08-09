package com.rhymartmanchus.yelpassignment.data.api.models

import com.google.gson.annotations.SerializedName

data class OpenHourRaw (

    @SerializedName("is_overnight")
    val isOvernight: Boolean,

    @SerializedName("start")
    val start: String,

    @SerializedName("end")
    val end: String,

    @SerializedName("day")
    val day: Int

)