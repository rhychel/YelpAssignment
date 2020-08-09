package com.rhymartmanchus.yelpassignment.domain.exceptions

class HttpRequestException (
    val httpCode: Int,
    val code: String,
    val errorMessage: String,
    val field: String?
) : Exception()