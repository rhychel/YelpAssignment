package com.rhymartmanchus.yelpassignment.ui

import com.rhymartmanchus.yelpassignment.domain.models.Address
import com.rhymartmanchus.yelpassignment.domain.models.ContactDetails
import com.rhymartmanchus.yelpassignment.domain.models.OperatingHour
import com.rhymartmanchus.yelpassignment.domain.models.Rating

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
        fun showHoursOfOperation(operations: List<OperatingHour>)
        fun showRating(rating: Rating)
        fun showReviews()

        fun callBusiness(phoneNumber: String)
        fun openMap(latitude: Double, longitude: Double)
    }

    interface Presenter {

        fun onViewCreated(name: String, alias: String)
        fun onCallBusinessClicked()
        fun onShowInMapClicked()

    }

}