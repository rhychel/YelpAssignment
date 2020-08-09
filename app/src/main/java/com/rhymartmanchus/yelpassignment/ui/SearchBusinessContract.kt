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

        fun hideSearchInvitation()

        fun showSortingButton()
        fun hideSortingButton()

        fun enlistResults(businesses: List<Business>)
        fun clearResults()

        fun setCategoriesButtonText(categoryTitle: String)

        fun proceedToCategories(category: Category?)
        fun proceedToBusinessDetails(business: Business)

    }

    interface Presenter {

        fun setNoCoordinates()
        fun takeCoordinates(coordinates: Coordinates)

        fun takeSearchKeyword(keyword: String?)
        fun invalidateSearchKeyword()

        fun takeCategory(category: Category)
        fun invalidateCategory()

        fun onGettingLocationStarted()

        fun takeSortingStrategy(sortingStrategy: SortingStrategy)

        fun onCategorySelected()
        fun onSortingStrategySelected()

        fun onCategoriesClicked()
        fun onSearchClicked()

        fun onBusinessClicked(business: Business)

    }

}