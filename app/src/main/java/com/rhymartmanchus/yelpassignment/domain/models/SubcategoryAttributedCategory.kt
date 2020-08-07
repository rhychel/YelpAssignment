package com.rhymartmanchus.yelpassignment.domain.models

data class SubcategoryAttributedCategory (
    val alias: String,
    val title: String,
    val subcategories: List<Category>
)