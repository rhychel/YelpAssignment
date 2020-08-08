package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.db.CategoriesDao
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class CategoriesLocalServiceProvider (
    private val dao: CategoriesDao
) : CategoriesLocalService {
    override suspend fun getSubcategoryAttributedCategoriesByAlias(
        alias: String,
        limit: Long,
        offset: Long
    ): List<SubcategoryAttributedCategory> =
        dao.getSubcategoryAttributedCategoriesByAlias(alias, limit, offset).map {
            it.toDomain()
        }.takeIf { it.isNotEmpty() }
            ?: throw NoDataException("No available categories with alias=`$alias`")

    override suspend fun getSubcategoryAttributedCategories(
        limit: Long,
        offset: Long
    ): List<SubcategoryAttributedCategory>  =
        dao.getSubcategoryAttributedCategories(limit, offset).map {
            it.toDomain()
        }.takeIf { it.isNotEmpty() }
            ?: throw NoDataException("No available categories")

    override suspend fun saveCategories(categories: List<Category>) {
        categories.forEach {
            dao.saveCategory(it.toDB())
            it.parentAliases.forEach { parent ->
                dao.saveCategoryAssoc(
                    CategoryAssocDB(
                        parent,
                        it.alias
                    )
                )
            }
        }
    }

    override suspend fun deleteCategories() {
        dao.deleteAllCategories()
    }

    override suspend fun deleteCategoryAssocs() {
        dao.deleteAllCategoryAssocs()
    }
}