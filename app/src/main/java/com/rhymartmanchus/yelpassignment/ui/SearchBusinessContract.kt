package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Category

interface SearchBusinessContract {

    interface View {

        fun showLoadingGroup()
        fun hideLoadingGroup()

        fun showNoResults()
        fun hideNoResults()

        fun enlistResults(businesses: List<Business>)

    }

    interface Presenter {

        fun setNoCoordinates()
        fun takeCoordinates(coordinates: Coordinates)

        fun takeSearchKeyword(keyword: String)
        fun invalidateSearchKeyword()

        fun takeCategory(category: Category)
        fun invalidateCategory()

        fun onGettingLocationStarted()

        fun onSearchClicked()
        fun onSortByNothingSelected()
        fun onSortByDistanceSelected()
        fun onSortByRatingSelected()

    }

}