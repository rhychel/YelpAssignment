package com.rhymartmanchus.yelpassignment.data.db.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB

data class SubcategoryAttributedCategoryDB (

    @Embedded
    val category: CategoryDB,

    @Relation(
        parentColumn = "alias",
        entityColumn = "alias",
        associateBy = Junction(
            CategoryAssocDB::class,
            parentColumn = "parent_alias",
            entityColumn = "child_alias"
        )
    )
    val subcategories: List<CategoryDB>

)