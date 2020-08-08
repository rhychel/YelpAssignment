package com.rhymartmanchus.yelpassignment.data.db

import androidx.room.*
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.models.SubcategoryAttributedCategoryDB

@Dao
interface CategoriesDao {

    @Transaction
    @Query("SELECT * FROM categories LIMIT :limit OFFSET :offset")
    suspend fun getSubcategoryAttributedCategories(limit: Long, offset: Long): List<SubcategoryAttributedCategoryDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategory(categoryDB: CategoryDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategoryAssoc(categoryAssocDB: CategoryAssocDB)

}