package com.rhymartmanchus.yelpassignment.domain.models

data class Business (
    val id: String,
    val alias: String,
    val name: String,
    val photoUrl: String,
    val categories: List<Category>,
    val operatingHours: List<OperatingHour>,
    val address: Address,
    val contactDetails: ContactDetails,
    val rating: Rating,
    val deals: List<Deals>,
    val distance: String,
    val isClosed: Boolean
)