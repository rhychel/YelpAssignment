package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Category

interface CategoriesRemoteService {

    suspend fun fetchCategoriesByLocale(locale: String): List<Category>

}