package com.rhymartmanchus.yelpassignment.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.FragmentCategoriesBinding
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import com.rhymartmanchus.yelpassignment.ui.viewmodels.CategoryItemVM
import eu.davidea.flexibleadapter.FlexibleAdapter

class CategoriesFragment (
    private val presenter: CategoriesContract.Presenter,
    private val onCategorySelectedListener: OnCategorySelectedListener,
    private val includeAllCategoriesItem: Boolean = false
) : Fragment(R.layout.fragment_categories), CategoriesContract.View {

    interface OnCategorySelectedListener {
        fun onSelected(subcategoryAttributedCategory: SubcategoryAttributedCategory)
    }

    private var parentAlias: String = ""

    private lateinit var binder: FragmentCategoriesBinding

    private val adapter: FlexibleAdapter<CategoryItemVM> by lazy {
        FlexibleAdapter(mutableListOf<CategoryItemVM>())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.takeView(this)

        binder = FragmentCategoriesBinding.bind(view)

        binder.rvCategories.setHasFixedSize(true)
        binder.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binder.rvCategories.adapter = adapter

        bindListeners()

        if(includeAllCategoriesItem)
            presenter.onViewCreatedForRoot()
        else
            presenter.onViewCreatedForSubCategories(parentAlias)
    }

    private fun bindListeners() {
        adapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { _, position ->
            onCategorySelectedListener.onSelected(adapter.getItem(position)!!.subcategoryAttributedCategory)

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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

}