package com.rhymartmanchus.yelpassignment.domain.models

sealed class Day (
    val label: String
) {

    object Monday : Day("MONDAY")
    object Tuesday : Day("TUESDAY")
    object Wednesday : Day("WEDNESDAY")
    object Thursday : Day("THURSDAY")
    object Friday : Day("FRIDAY")
    object Saturday : Day("SATURDAY")
    object Sunday : Day("SUNDAY")
    object Undetermined : Day("Undetermined")

    companion object {

        fun determineDay(day: Int): Day =
            when(day) {
                0 -> Monday
                1 -> Tuesday
                2 -> Wednesday
                3 -> Thursday
                4 -> Friday
                5 -> Saturday
                6 -> Sunday
                else -> Undetermined
            }

    }

}