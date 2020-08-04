package com.rhymartmanchus.yelpassignment.domain

sealed class SortingStrategy (
    val key: String
) {

    object Default : SortingStrategy("")
    object Rating : SortingStrategy("rating")
    object Distance : SortingStrategy("distance")

}