package com.rhymartmanchus.yelpassignment.data.db

import androidx.room.*
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.models.SubcategoryAttributedCategoryDB

@Dao
interface CategoriesDao {

    @Transaction
    @Query("SELECT * FROM categories LIMIT :limit OFFSET :offset")
    suspend fun getSubcategoryAttributedCategories(
        limit: Long, offset: Long
    ): List<SubcategoryAttributedCategoryDB>

    @Transaction
    @Query("SELECT c.* FROM category_assoc ca, categories c WHERE ca.parent_alias = :alias AND c.alias = ca.child_alias LIMIT :limit OFFSET :offset")
    suspend fun getSubcategoryAttributedCategoriesByAlias(
        alias: String, limit: Long, offset: Long
    ): List<SubcategoryAttributedCategoryDB>

    @Insert
    suspend fun saveCategory(categoryDB: CategoryDB)

    @Insert
    suspend fun saveCategoryAssoc(categoryAssocDB: CategoryAssocDB)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM category_assoc")
    suspend fun deleteAllCategoryAssocs()

}