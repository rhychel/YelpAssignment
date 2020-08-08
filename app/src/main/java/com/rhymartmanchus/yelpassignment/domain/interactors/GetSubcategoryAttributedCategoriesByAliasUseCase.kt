package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class GetSubcategoryAttributedCategoriesByAliasUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: CategoriesGateway
) : UseCase<GetSubcategoryAttributedCategoriesByAliasUseCase.Response, GetSubcategoryAttributedCategoriesByAliasUseCase.Params>(appCoroutinesDispatcher) {

    override suspend fun run(params: Params): Response =
        Response(
            gateway.getSubcategoryAttributedCategoryByAlias(
                params.alias,
                params.limit,
                params.offset
            )
        )

    data class Params (
        val alias: String,
        val limit: Long,
        val offset: Long
    )

    data class Response (
        val subcategoryAttributedCategories: List<SubcategoryAttributedCategory>
    )

}