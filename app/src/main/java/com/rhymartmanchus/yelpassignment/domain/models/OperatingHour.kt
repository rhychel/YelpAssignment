package com.rhymartmanchus.yelpassignment.domain.models

data class OperatingHour (
    val isOvernight: Boolean,
    val start: String,
    val end: String,
    val day: Day
)
