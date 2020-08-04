package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Address
import com.rhymartmanchus.yelpassignment.domain.models.ContactDetails
import com.rhymartmanchus.yelpassignment.domain.models.Establishment
import com.rhymartmanchus.yelpassignment.domain.models.Rating

fun BusinessRaw.toDomain(): Establishment =
    Establishment(
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