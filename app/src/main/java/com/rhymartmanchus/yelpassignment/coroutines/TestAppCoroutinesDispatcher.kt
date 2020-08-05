package com.rhymartmanchus.yelpassignment.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TestAppCoroutinesDispatcher : AppCoroutineDispatcher {
    override fun io(): CoroutineContext = Dispatchers.Unconfined
    override fun ui(): CoroutineContext = Dispatchers.Unconfined
}