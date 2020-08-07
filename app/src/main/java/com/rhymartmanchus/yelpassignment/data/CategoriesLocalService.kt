package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

interface CategoriesLocalService {

    suspend fun getSubcategoryAttributedCategories(): List<SubcategoryAttributedCategory>
    suspend fun saveCategories(categories: List<Category>)

}