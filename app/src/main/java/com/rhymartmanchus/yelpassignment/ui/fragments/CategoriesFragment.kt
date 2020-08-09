package com.rhymartmanchus.yelpassignment.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.FragmentCategoriesBinding
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import com.rhymartmanchus.yelpassignment.ui.viewmodels.BaseVM
import com.rhymartmanchus.yelpassignment.ui.viewmodels.CategoryItemVM
import com.rhymartmanchus.yelpassignment.ui.viewmodels.ProgressItemVM
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

class CategoriesFragment (
    private val presenter: CategoriesContract.Presenter,
    private val onCategorySelectedListener: OnCategorySelectedListener,
    private val includeAllCategoriesItem: Boolean = false
) : Fragment(R.layout.fragment_categories), CategoriesContract.View {

    private companion object {
        const val LIMIT = 20
        var PAGE = 0
    }

    interface OnCategorySelectedListener {
        fun onSelected(subcategoryAttributedCategory: SubcategoryAttributedCategory)
    }

    private var parentAlias: String = ""

    private lateinit var binder: FragmentCategoriesBinding

    private val adapter by lazy {
        FlexibleAdapter(mutableListOf<AbstractFlexibleItem<*>>())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.takeView(this)

        binder = FragmentCategoriesBinding.bind(view)

        initializeRecyclerView()
        bindListeners()

        if(includeAllCategoriesItem)
            presenter.onViewCreatedForRoot()
        else
            presenter.onViewCreatedForSubCategories(parentAlias)
    }

    private fun initializeRecyclerView() {

        binder.rvCategories.setHasFixedSize(true)
        binder.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binder.rvCategories.adapter = adapter

        adapter.setEndlessScrollThreshold(LIMIT)
        adapter.setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
            override fun noMoreLoad(newItemsSize: Int) {
            }

            override fun onLoadMore(lastPosition: Int, currentPage: Int) {
                if(includeAllCategoriesItem)
                    loadMoreCategories(lastPosition)
                else
                    loadMoreSubcategories(lastPosition)
            }

        }, ProgressItemVM())
    }

    private fun loadMoreCategories(lastPosition: Int) {
        if((lastPosition -1) % LIMIT == 0) {
            presenter.onLoadMoreCategories()
        }
        else
            stopEndlessScrolling()
    }

    private fun loadMoreSubcategories(lastPosition: Int) {
        if(lastPosition % LIMIT == 0) {
            presenter.onLoadMoreSubcategories()
        }
        else
            stopEndlessScrolling()
    }

    private fun bindListeners() {
        adapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { _, position ->
            val item = adapter.getItem(position)!! as CategoryItemVM
            onCategorySelectedListener.onSelected(item.subcategoryAttributedCategory)

            true
        }
    }

    override fun takeParentAlias(alias: String) {
        this.parentAlias = alias
    }

    override fun enlistCategories(categories: List<SubcategoryAttributedCategory>) {
        adapter.updateDataSet(categories.map { CategoryItemVM(it) })
        adapter.notifyDataSetChanged()
    }

    override fun appendCategories(categories: List<SubcategoryAttributedCategory>) {
        PAGE++
        adapter.onLoadMoreComplete(categories.map { CategoryItemVM(it) })
    }

    override fun showProgressItem() {
        adapter.addItem(ProgressItemVM())
    }

    override fun stopEndlessScrolling() {
        adapter.onLoadMoreComplete(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

}