package com.rhymartmanchus.yelpassignment.domain.exceptions

class BusinessNotFoundException (
    val alias: String
) : Exception("Business with alias=`$alias` does not exist")