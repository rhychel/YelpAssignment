package com.rhymartmanchus.yelpassignment.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CoroutineDispatcher : AppCoroutineDispatcher {
    override fun io(): CoroutineContext = Dispatchers.IO
    override fun ui(): CoroutineContext = Dispatchers.Main
}