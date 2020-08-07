package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.db.CategoriesDao
import com.rhymartmanchus.yelpassignment.data.db.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class CategoriesLocalServiceProvider (
    private val dao: CategoriesDao
) : CategoriesLocalService {

    override suspend fun getSubcategoryAttributedCategories(): List<SubcategoryAttributedCategory> =
        dao.getSubcategoryAttributedCategories().map {
            it.toDomain()
        }

    override suspend fun saveCategories(categories: List<Category>) {
        categories.forEach {
            dao.saveCategory(it.toDB())
            it.parentAliases.forEach { parent ->
                dao.saveCategoryAssoc(
                    CategoryAssocDB(parent, it.alias)
                )
            }
        }
    }
}