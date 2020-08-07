package com.rhymartmanchus.yelpassignment.domain.models

data class Category (
    val alias: String,
    val title: String,
    val parentAliases: List<String>
)