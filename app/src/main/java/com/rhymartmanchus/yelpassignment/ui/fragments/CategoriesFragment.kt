package com.rhymartmanchus.yelpassignment.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.FragmentCategoriesBinding
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import com.rhymartmanchus.yelpassignment.ui.adapters.CategoriesAdapter

class CategoriesFragment (
    private val presenter: CategoriesContract.Presenter,
    private val onCategorySelectedListener: CategoriesAdapter.OnCategorySelectedListener,
    private val includeAllCategoriesItem: Boolean = false
) : Fragment(R.layout.fragment_categories), CategoriesContract.View {

    private var parentAlias: String = ""

    private lateinit var binder: FragmentCategoriesBinding

    private val adapter: CategoriesAdapter by lazy {
        CategoriesAdapter(
            mutableListOf(),
            onCategorySelectedListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.takeView(this)

        binder = FragmentCategoriesBinding.bind(view)

        binder.rvCategories.setHasFixedSize(true)
        binder.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binder.rvCategories.adapter = adapter

        if(includeAllCategoriesItem)
            presenter.onViewCreatedForRoot()
        else
            presenter.onViewCreatedForSubCategories(parentAlias)
    }

    override fun takeParentAlias(alias: String) {
        this.parentAlias = alias
    }

    override fun enlistCategories(categories: List<SubcategoryAttributedCategory>) {
        adapter.addAll(categories)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

}