package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.Coordinates
import com.rhymartmanchus.yelpassignment.domain.models.Establishment

interface BusinessesGateway {

    fun fetchBusinessesInArea(coordinates: Coordinates): List<Establishment>
    fun searchBusinessesInAreaByKeyword(coordinates: Coordinates, keyword: String,
                                        sortingStrategy: SortingStrategy): List<Establishment>
    fun searchBusinessesByCategoryInAreaForKeyword(coordinates: Coordinates, category: Category,
                                                   sortingStrategy: SortingStrategy): List<Establishment>

}