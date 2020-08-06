package com.rhymartmanchus.yelpassignment.domain.exceptions

import java.io.IOException

class NetworkErrorException (
    private val ioException: IOException
) : Exception()