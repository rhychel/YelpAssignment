package com.rhymartmanchus.yelpassignment.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category (
    val alias: String,
    val title: String,
    val parentAliases: List<String>
) : Parcelable