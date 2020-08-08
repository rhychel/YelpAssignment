package com.rhymartmanchus.yelpassignment.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "category_assoc",
    primaryKeys = ["child_alias", "parent_alias"]
)
data class CategoryAssocDB (

    @ColumnInfo(
        name = "parent_alias"
    )
    val parentAlias: String,

    @ColumnInfo(
        name = "child_alias"
    )
    val childAlias: String

)