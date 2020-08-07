package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class GetSubcategoryAttributedCategoriesUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: CategoriesGateway
) : UseCase<GetSubcategoryAttributedCategoriesUseCase.Response, GetSubcategoryAttributedCategoriesUseCase.Params>(appCoroutinesDispatcher) {

    override suspend fun run(params: Params): Response =
        Response(
            gateway.getSubcategoryAttributedCategory(
                params.limit,
                params.offset
            )
        )

    data class Params (
        val limit: Long,
        val offset: Long
    )

    data class Response (
        val subcategoryAttributedCategories: List<SubcategoryAttributedCategory>
    )

}