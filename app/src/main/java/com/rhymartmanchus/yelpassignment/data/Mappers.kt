package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import com.rhymartmanchus.yelpassignment.domain.models.*

fun BusinessRaw.toDomain(): Business =
    Business(
        "Name",
        "URL",
        emptyList(),
        emptyList(),
        Address(
            "sample", null, null
        ),
        ContactDetails(
            "09778195952",
            "+63 977 819 5952"
        ),
        Rating(4.5, "Reviews from 1000 users"),
        emptyList()
    )

fun CategoryRaw.toDomain(): Category =
    Category(
        alias,
        title,
        parentAliases
    )