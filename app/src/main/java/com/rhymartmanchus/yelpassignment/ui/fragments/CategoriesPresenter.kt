package com.rhymartmanchus.yelpassignment.ui.fragments

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.interactors.GetSubcategoryAttributedCategoriesUseCase
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CategoriesPresenter (
    private val dispatcher: AppCoroutineDispatcher,
    private val getSubcategoryAttributedCategoriesUseCase: GetSubcategoryAttributedCategoriesUseCase
) : CategoriesContract.Presenter, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.io()

    private var view: CategoriesContract.View? = null

    override fun takeView(view: CategoriesContract.View) {
        this.view = view
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun onViewCreated() {
        launch {
            val result = getSubcategoryAttributedCategoriesUseCase.execute(
                GetSubcategoryAttributedCategoriesUseCase.Params(20, 0)
            )
            withContext(dispatcher.ui()) {
                view?.enlistCategories(result.subcategoryAttributedCategories)
            }
        }
    }

}