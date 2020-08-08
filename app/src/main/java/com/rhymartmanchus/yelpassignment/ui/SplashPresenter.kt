package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
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

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = dispatcher.io() + job

    override fun onLocationIsAllowed() {
        launch {
            try {
//                fetchCategoriesByLocaleUseCase.execute(
//                    FetchCategoriesByLocaleUseCase.Param(
//                        Locale.getDefault()
//                    )
//                )

                withContext(dispatcher.ui()) {
                    view.proceedToNext()
                }
            } catch (e: NetworkErrorException) {
                withContext(dispatcher.ui()) {
                    view.popupNetworkFailedDialog()
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

    override fun onViewDestroyed() {
        job.cancel()
    }

}