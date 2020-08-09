package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import com.rhymartmanchus.yelpassignment.data.api.models.ReviewRaw
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.models.SubcategoryAttributedCategoryDB
import com.rhymartmanchus.yelpassignment.domain.models.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

private fun timeParser(time: String): String {
    val hour = time.substring(0, 2)
    val minutes = time.substring(2, 4)
    val median = if(hour.toInt() >= 12) "PM" else "AM"

    if(hour.toInt() == 0)
        return "12:$minutes $median"
    return "${hour.toInt().takeIf { it < 13 } ?: hour.toInt()-12}:$minutes $median"
}

fun BusinessRaw.toDomain(): Business =
    Business(
        id,
        alias,
        name,
        imageUrl,
        categories.map { it.toDomain() },
        openHours.map {
            OperatingHour(
                it.isOvernight,
                timeParser(it.start),
                timeParser(it.end),
                Day.determineDay(it.day)
            )
        },
        Address(
            fullAddress, latitude, longitude
        ),
        ContactDetails(
            phone,
            displayPhone
        ),
        Rating(rating, "$reviewCount Reviews"),
        emptyList(),
        "${"%.2f".format(BigDecimal(distance/1000.0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toDouble())} km",
        isClosed
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

fun ReviewRaw.toDomain(): Review =
    Review(
        rating,
        timeCreatedParser(timeCreated),
        userName,
        userImageUrl ?: "",
        text
    )

private fun timeCreatedParser(timeCreated: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    val datetime = formatter.parse(timeCreated)

    val display = SimpleDateFormat("MMM dd, yyyy hh:mma")
    return display.format(datetime)
}