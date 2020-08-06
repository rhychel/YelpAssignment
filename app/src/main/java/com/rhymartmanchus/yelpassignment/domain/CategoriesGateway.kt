package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Category

interface CategoriesGateway {

    suspend fun fetchCategories(yelpLocale: YelpLocale): List<Category>

}
