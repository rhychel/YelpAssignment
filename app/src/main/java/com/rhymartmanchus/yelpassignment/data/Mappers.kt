package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.models.SubcategoryAttributedCategoryDB
import com.rhymartmanchus.yelpassignment.domain.models.*

fun BusinessRaw.toDomain(): Business =
    Business(
        "Name",
        "URL",
        emptyList(),
        emptyList(),
        Address(
            "sample", null, null
        ),
        ContactDetails(
            "09778195952",
            "+63 977 819 5952"
        ),
        Rating(4.5, "Reviews from 1000 users"),
        emptyList()
    )

fun CategoryRaw.toDomain(): Category =
    Category(
        alias,
        title,
        parentAliases
    )

fun Category.toDB(): CategoryDB =
    CategoryDB(
        alias,
        title
    )

fun SubcategoryAttributedCategoryDB.toDomain(): SubcategoryAttributedCategory =
    SubcategoryAttributedCategory(
        category.alias,
        category.title,
        subcategories.map { it.toDomain(listOf(category.alias)) }
    )

fun CategoryDB.toDomain(parents: List<String>): Category =
    Category(
        alias,
        title,
        parents
    )
