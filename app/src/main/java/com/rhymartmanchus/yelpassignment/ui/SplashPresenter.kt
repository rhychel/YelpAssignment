package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.interactors.FetchCategoriesByLocaleUseCase
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashPresenter (
    private val dispatcher: AppCoroutineDispatcher,
    private val view: SplashContract.View,
    private val fetchCategoriesByLocaleUseCase: FetchCategoriesByLocaleUseCase
) : SplashContract.Presenter, CoroutineScope {

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.io()

    override fun onLocationIsAllowed() {

        launch {
            try {
                fetchCategoriesByLocaleUseCase.execute(
                    FetchCategoriesByLocaleUseCase.Param(
                        Locale.getDefault()
                    )
                )

                withContext(dispatcher.ui()) {
                    view.proceedToNext()
                }
            } catch (e: Exception) {
                withContext(dispatcher.ui()) {
                    when(e) {
                        is NoDataException -> view.popupNetworkFailedDialog()
                        else -> throw e
                    }
                }
            }

        }

    }

    override fun onRetryClicked() {
        onLocationIsAllowed()
    }

    override fun onExitClicked() {
        view.closeView()
    }

}