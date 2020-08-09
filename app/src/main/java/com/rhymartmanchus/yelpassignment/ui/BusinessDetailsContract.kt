package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.domain.models.*

interface BusinessDetailsContract {

    interface View {

        fun popupLoadingDialog()
        fun dismissLoadingDialog()

        fun popupNetworkFailureDialog()
        fun popupBusinessIsMigratedDialog(alias: String)
        fun popupBusinessNotFoundDialog(name: String)

        fun showNameAndPhoto(name: String, photoUrl: String)
        fun showCategories(categories: List<String>)
        fun showContactDetails(contactDetails: ContactDetails)
        fun showAddress(address: Address)
        fun showOpenHours(operations: List<OperatingHour>)
        fun showRating(rating: Rating)
        fun showReviews(reviews: List<Review>)

        fun callBusiness(phoneNumber: String)
        fun openMap(latitude: Double, longitude: Double)
    }

    interface Presenter {

        fun onViewCreated(name: String, alias: String)
        fun onCallBusinessClicked()
        fun onShowInMapClicked()

    }

}