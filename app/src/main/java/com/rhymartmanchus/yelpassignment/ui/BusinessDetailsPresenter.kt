package com.rhymartmanchus.yelpassignment.ui

import android.accounts.NetworkErrorException
import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessMigratedException
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessNotFoundException
import com.rhymartmanchus.yelpassignment.domain.interactors.FetchBusinessByAliasUseCase
import com.rhymartmanchus.yelpassignment.domain.models.Business
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BusinessDetailsPresenter (
    private val dispatcher: AppCoroutineDispatcher,
    private val view: BusinessDetailsContract.View,
    private val fetchBusinessByAliasUseCase: FetchBusinessByAliasUseCase
) : BusinessDetailsContract.Presenter, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.io()

    private lateinit var name: String
    private lateinit var alias: String
    private lateinit var business: Business

    override fun onViewCreated(name: String, alias: String) {
        this.name = name
        this.alias = alias

        view.popupLoadingDialog()
        launch {

            try {
                business = fetchBusinessByAliasUseCase.execute(
                    FetchBusinessByAliasUseCase.Param(alias)
                ).business

                withContext(dispatcher.ui()) {
                    view.dismissLoadingDialog()
                    with(business) {
                        view.showNameAndPhoto(name, photoUrl)
                        view.showCategories(categories.map { it.title })
                        view.showAddress(address)
                        view.showContactDetails(contactDetails)
                        view.showOpenHours(operatingHours)
                        view.showRating(rating)
                    }
                }
            } catch (e: NetworkErrorException) {
                renderNetworkErrorDialog()
            } catch (e: BusinessNotFoundException) {
                renderBusinessNotFoundDialog()
            } catch (e: BusinessMigratedException) {
                renderBusinessIsMigratedDialog()
            }
        }
    }

    private suspend fun renderNetworkErrorDialog() {
        view.dismissLoadingDialog()
        withContext(dispatcher.ui()) {
            view.popupNetworkFailureDialog()
        }
    }

    private suspend fun renderBusinessNotFoundDialog() {
        view.dismissLoadingDialog()
        withContext(dispatcher.ui()) {
            view.popupBusinessNotFoundDialog(name)
        }
    }

    private suspend fun renderBusinessIsMigratedDialog() {
        view.dismissLoadingDialog()
        withContext(dispatcher.ui()) {
            view.popupBusinessIsMigratedDialog(alias)
        }
    }

    override fun onCallBusinessClicked() {
        view.callBusiness(business.contactDetails.phoneNumber)
    }

    override fun onShowInMapClicked() {
        view.openMap(
            business.address.latitude!!,
            business.address.longitude!!
        )
    }


}