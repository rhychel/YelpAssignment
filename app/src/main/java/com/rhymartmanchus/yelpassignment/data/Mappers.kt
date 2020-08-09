package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.models.SubcategoryAttributedCategoryDB
import com.rhymartmanchus.yelpassignment.domain.models.*
import java.math.BigDecimal

fun BusinessRaw.toDomain(): Business =
    Business(
        id,
        name,
        imageUrl,
        categories.map { it.toDomain() },
        emptyList(),
        Address(
            fullAddress, latitude, longitude
        ),
        ContactDetails(
            phone,
            displayPhone
        ),
        Rating(rating, "$reviewCount Reviews"),
        emptyList(),
        "${"%.2f".format(BigDecimal(distance/1000.0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toDouble())} km"
    )

fun CategoryRaw.toDomain(): Category =
    Category(
        alias,
        title,
        parentAliases ?: emptyList()
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
