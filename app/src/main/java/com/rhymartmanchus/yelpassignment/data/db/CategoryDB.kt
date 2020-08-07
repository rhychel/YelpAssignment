package com.rhymartmanchus.yelpassignment.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories"
)
data class CategoryDB (

    @PrimaryKey
    @ColumnInfo(
        name = "alias"
    )
    val alias: String,

    @ColumnInfo(
        name = "title"
    )
    val title: String
)