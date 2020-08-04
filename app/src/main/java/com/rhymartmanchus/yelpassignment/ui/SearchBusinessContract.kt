package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.domain.models.Coordinates

interface SearchBusinessContract {

    interface View {



    }

    interface Presenter {

        fun takeCoordinates(coordinates: Coordinates)

        fun onSearchByBusinessNameClicked(keyword: String)
        fun onSortByNothingSelected()
        fun onSortByDistanceSelected()
        fun onSortByRatingSelected()

    }

}