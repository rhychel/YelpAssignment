package com.rhymartmanchus.yelpassignment.domain.models

data class Review (
    val rating: Double,
    val timeCreated: String,
    val userName: String,
    val userPicUrl: String,
    val snippet: String
)
