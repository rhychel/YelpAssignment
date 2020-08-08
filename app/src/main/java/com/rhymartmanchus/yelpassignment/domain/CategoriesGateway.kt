package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

interface CategoriesGateway {

    suspend fun fetchCategories(yelpLocale: YelpLocale): List<Category>
    suspend fun getSubcategoryAttributedCategory(limit: Long, offset: Long): List<SubcategoryAttributedCategory>
    suspend fun saveCategories(categories: List<Category>)
    suspend fun invalidateCategories()
    suspend fun getSubcategoryAttributedCategoryByAlias(
        alias: String,
        limit: Long,
        offset: Long
    ): List<SubcategoryAttributedCategory>

}
