package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<Response, Params> (
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) {

    protected abstract suspend fun run(params: Params): Response

    suspend fun execute(params: Params): Response = withContext(appCoroutineDispatcher.io()) {
        run(params)
    }

}