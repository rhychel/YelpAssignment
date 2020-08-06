package com.rhymartmanchus.yelpassignment.domain.exceptions

class MaxLimitParamException (
    private val maxLimit: Int
) : Exception("params.limit cannot be greater than $maxLimit")