package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.YelpLocale
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class CategoriesRepository (
    private val local: CategoriesLocalService,
    private val remote: CategoriesRemoteService
) : CategoriesGateway {

    override suspend fun fetchCategories(yelpLocale: YelpLocale): List<Category> =
        remote.fetchCategoriesByLocale(yelpLocale.locale)

    override suspend fun getSubcategoryAttributedCategory(
        limit: Long,
        offset: Long
    ): List<SubcategoryAttributedCategory> =
        local.getSubcategoryAttributedCategories(limit, offset)

    override suspend fun saveCategories(categories: List<Category>) =
        local.saveCategories(categories)

    override suspend fun invalidateCategories() {
        local.deleteCategories()
        local.deleteCategoryAssocs()
    }

    override suspend fun getSubcategoryAttributedCategoryByAlias(
        alias: String,
        limit: Long,
        offset: Long
    ): List<SubcategoryAttributedCategory> =
        local.getSubcategoryAttributedCategoriesByAlias(
            alias, limit, offset
        )
}