package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.YelpLocale
import com.rhymartmanchus.yelpassignment.domain.models.Category
import java.util.*

class FetchCategoriesByLocaleUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: CategoriesGateway
) : UseCase<FetchCategoriesByLocaleUseCase.Response,
        FetchCategoriesByLocaleUseCase.Param>(appCoroutinesDispatcher) {

    override suspend fun run(params: Param): Response {
        val yelpLocale = YelpLocale (
            params.locale.toYelpLocaleFormat()
        )

        return Response(
            gateway.fetchCategories(yelpLocale)
        )
    }

    private fun Locale.toYelpLocaleFormat(): String = toLanguageTag().replace("-", "_")

    data class Param (
        val locale: Locale
    )

    data class Response (
        val categories: List<Category>
    )

}