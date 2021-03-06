package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.interactors.FetchBusinessesByLocationUseCase
import com.rhymartmanchus.yelpassignment.domain.interactors.FetchBusinessesInCategoryByLocationUseCase
import com.rhymartmanchus.yelpassignment.domain.interactors.SearchBusinessesByKeywordUseCase
import com.rhymartmanchus.yelpassignment.domain.interactors.SearchBusinessesInCategoryByKeywordUseCase
import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Category
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchBusinessPresenter (
    private val dispatcher: AppCoroutineDispatcher,
    private val view: SearchBusinessContract.View,
    private val fetchBusinessesByLocationUseCase: FetchBusinessesByLocationUseCase,
    private val searchBusinessesByKeywordUseCase: SearchBusinessesByKeywordUseCase,
    private val fetchBusinessesInCategoryByLocationUseCase: FetchBusinessesInCategoryByLocationUseCase,
    private val searchBusinessesInCategoryByKeywordUseCase: SearchBusinessesInCategoryByKeywordUseCase
) : SearchBusinessContract.Presenter, CoroutineScope {

    private companion object {
        const val LIMIT = 20
    }
    private var OFFSET = 0

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.io()

    private var category: Category? = null
    private var keyword: String? = null
    private var coordinates: Coordinates? = null
    private var sortingStrategy: SortingStrategy = SortingStrategy.Default

    override fun setNoCoordinates() {
        coordinates = null
    }

    override fun takeCoordinates(coordinates: Coordinates) {
        this.coordinates = coordinates
    }

    override fun takeSearchKeyword(keyword: String?) {
        this.keyword = keyword
    }

    override fun invalidateSearchKeyword() {
        keyword = null
    }

    override fun takeCategory(category: Category) {
        this.category = category
    }

    override fun invalidateCategory() {
        category = null
    }

    override fun onGettingLocationStarted() {
    }

    override fun onSearchClicked() {
        loadResults()
    }

    override fun onBusinessClicked(business: Business) {
        view.proceedToBusinessDetails(business)
    }

    override fun onLoadMoreBusinesses() {
        OFFSET += LIMIT
        launch {
            try {
                displaySearchResults()
            } catch (e: NoDataException) {
                withContext(dispatcher.ui()) {
                    view.stopEndlessScrolling()
                }
            } catch (e: NetworkErrorException) {
                // network issue
            }
        }
    }

    private fun loadResults() {
        OFFSET = 0
        view.hideNoResults()
        view.hideSearchInvitation()
        view.showLoadingGroup()
        launch {
            if(coordinates == null) {
                delay(500L)
                displayNoResults()
            }
            else {
                try {
                    displaySearchResults()
                } catch (e: NoDataException) {
                    displayNoResults()
                } catch (e: NetworkErrorException) {
                    // network issue
                }
            }
        }
    }

    private suspend fun displayNoResults() {
        withContext(dispatcher.ui()) {
            view.hideSortingButton()
            view.showNoResults()
            view.hideLoadingGroup()
            view.clearResults()
        }
    }

    private suspend fun displaySearchResults() {
        if(category != null) {
            if(keyword != null) {
                searchBusinessesInCategory()
            }
            else {
                fetchBusinessesInCategory()
            }
        }
        else {
            if(keyword != null) {
                searchBusinesses()
            }
            else {
                fetchBusinesses()
            }
        }
        withContext(dispatcher.ui()) {
            view.showSortingButton()
            view.hideLoadingGroup()
        }
    }

    private suspend fun renderBusinessesResults(businesses: List<Business>) {
        withContext(dispatcher.ui()) {
            if(OFFSET > 0)
                view.appendResults(businesses)
            else
                view.enlistResults(businesses)
        }
    }

    private suspend fun searchBusinessesInCategory() {
        val result = searchBusinessesInCategoryByKeywordUseCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                keyword!!,
                category!!,
                LIMIT,
                OFFSET,
                coordinates!!.latitude,
                coordinates!!.longitude,
                sortingStrategy
            )
        )

        renderBusinessesResults(result.businesses)
    }

    private suspend fun searchBusinesses() {
        val result = searchBusinessesByKeywordUseCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                keyword!!,
                LIMIT,
                OFFSET,
                coordinates!!.latitude,
                coordinates!!.longitude,
                sortingStrategy
            )
        )

        renderBusinessesResults(result.businesses)
    }

    private suspend fun fetchBusinessesInCategory() {
        val result = fetchBusinessesInCategoryByLocationUseCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category!!,
                LIMIT,
                OFFSET,
                coordinates!!.latitude,
                coordinates!!.longitude,
                sortingStrategy
            )
        )

        renderBusinessesResults(result.businesses)
    }

    private suspend fun fetchBusinesses() {
        val result = fetchBusinessesByLocationUseCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                LIMIT,
                OFFSET,
                coordinates!!.latitude,
                coordinates!!.longitude,
                sortingStrategy
            )
        )

        renderBusinessesResults(result.businesses)
    }

    override fun takeSortingStrategy(sortingStrategy: SortingStrategy) {
        this.sortingStrategy = sortingStrategy
    }

    override fun onCategoriesClicked() {
        view.proceedToCategories(category)
    }

    override fun onCategorySelected() {
        view.setCategoriesButtonText(category?.title ?: "All Categories")
        loadResults()
    }

    override fun onSortingStrategySelected() {
        loadResults()
    }

}