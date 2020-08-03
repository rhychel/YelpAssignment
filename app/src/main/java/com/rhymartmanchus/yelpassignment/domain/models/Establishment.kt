package com.rhymartmanchus.yelpassignment.domain.models

data class Establishment (
    val name: String,
    val photoUrl: String,
    val categories: List<Category>,
    val operatingHours: List<OperatingHour>,
    val address: Address,
    val contactDetails: ContactDetails,
    val ratings: Rating,
    val deals: List<Deals>
)