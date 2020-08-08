package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

interface CategoriesLocalService {

    suspend fun getSubcategoryAttributedCategories(limit: Long, offset: Long): List<SubcategoryAttributedCategory>
    suspend fun saveCategories(categories: List<Category>)
    suspend fun deleteCategories()
    suspend fun deleteCategoryAssocs()

}