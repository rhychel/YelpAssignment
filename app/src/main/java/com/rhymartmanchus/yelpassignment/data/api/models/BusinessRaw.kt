package com.rhymartmanchus.yelpassignment.data.api.models

import com.google.gson.annotations.SerializedName

data class BusinessRaw (

    @SerializedName("id")
    val id: String,

    @SerializedName("alias")
    val alias: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("review_count")
    val reviewCount: Int,

    @SerializedName("categories")
    val categories: List<CategoryRaw>,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("display_phone")
    val displayPhone: String,

    @SerializedName("distance")
    val distance: Double,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("is_closed")
    val isClosed: Boolean,

    val fullAddress: String,
    val latitude: Double?,
    val longitude: Double?,
    val openHours: List<OpenHourRaw>

)