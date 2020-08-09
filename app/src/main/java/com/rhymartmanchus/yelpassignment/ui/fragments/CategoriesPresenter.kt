package com.rhymartmanchus.yelpassignment.ui.fragments

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.interactors.GetSubcategoryAttributedCategoriesByAliasUseCase
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
    private val getSubcategoryAttributedCategoriesUseCase: GetSubcategoryAttributedCategoriesUseCase,
    private val getSubcategoryAttributedCategoriesByAliasUseCase: GetSubcategoryAttributedCategoriesByAliasUseCase
) : CategoriesContract.Presenter, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.io()
    private var view: CategoriesContract.View? = null
    private companion object {
        const val LIMIT = 20L
    }
    private var OFFSET = 0L
    private var alias: String = ""

    override fun takeView(view: CategoriesContract.View) {
        this.view = view
    }

    override fun onViewCreatedForRoot() {
        OFFSET = 0L
        launch {
            val result = getSubcategoryAttributedCategoriesUseCase.execute(
                GetSubcategoryAttributedCategoriesUseCase.Params(LIMIT, OFFSET)
            )
            withContext(dispatcher.ui()) {
                val list = mutableListOf(
                    *result.subcategoryAttributedCategories.toTypedArray()
                )
                list.add(0, SubcategoryAttributedCategory(
                    "yelph-all",
                    "All Categories",
                    emptyList()
                ))
                view?.enlistCategories(list)
            }
        }
    }

    override fun onViewCreatedForSubCategories(alias: String) {
        OFFSET = 0L
        this.alias = alias
        launch {
            val result = getSubcategoryAttributedCategoriesByAliasUseCase.execute(
                GetSubcategoryAttributedCategoriesByAliasUseCase.Params(alias, LIMIT, OFFSET)
            )
            withContext(dispatcher.ui()) {
                view?.enlistCategories(result.subcategoryAttributedCategories)
            }
        }
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun onLoadMoreCategories() {
        OFFSET += LIMIT
        launch {
            try {
                val result = getSubcategoryAttributedCategoriesUseCase.execute(
                    GetSubcategoryAttributedCategoriesUseCase.Params(LIMIT, OFFSET)
                )
                withContext(dispatcher.ui()) {
                    val list = mutableListOf(
                        *result.subcategoryAttributedCategories.toTypedArray()
                    )
                    view?.appendCategories(list)
                }
            } catch (e: NoDataException) {
                withContext(dispatcher.ui()) {
                    view?.stopEndlessScrolling()
                }
            }
        }
    }

    override fun onLoadMoreSubcategories() {
        OFFSET += LIMIT
        launch {
            try {
                val result = getSubcategoryAttributedCategoriesByAliasUseCase.execute(
                    GetSubcategoryAttributedCategoriesByAliasUseCase.Params(alias, LIMIT, OFFSET)
                )
                withContext(dispatcher.ui()) {
                    val list = mutableListOf(
                        *result.subcategoryAttributedCategories.toTypedArray()
                    )
                    view?.appendCategories(list)
                }
            } catch (e: NoDataException) {
                withContext(dispatcher.ui()) {
                    view?.stopEndlessScrolling()
                }
            }
        }
    }

}