package com.rhymartmanchus.yelpassignment.ui

interface SplashContract {

    interface View {

        fun popupNetworkFailedDialog()

        fun proceedToNext()
        fun closeView()

    }

    interface Presenter {

        fun onLocationIsAllowed()

        fun onRetryClicked()
        fun onExitClicked()

        fun onViewDestroyed()

    }

}