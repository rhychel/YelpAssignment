package com.rhymartmanchus.yelpassignment.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SubcategoryAttributedCategoryDB (

    @Embedded
    val category: CategoryDB,

    @Relation(
        parentColumn = "alias",
        entityColumn = "alias",
        associateBy = Junction(
            CategoryAssocDB::class,
            parentColumn = "child_alias",
            entityColumn = "parent_alias"
        )
    )
    val subcategories: List<CategoryDB>

)