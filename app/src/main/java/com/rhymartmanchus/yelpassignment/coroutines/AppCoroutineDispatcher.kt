package com.rhymartmanchus.yelpassignment.coroutines

import kotlin.coroutines.CoroutineContext

interface AppCoroutineDispatcher {

    fun io(): CoroutineContext
    fun ui(): CoroutineContext

}