package com.rhymartmanchus.yelpassignment.ui.fragments

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

interface CategoriesContract {

    interface View {

        fun takeParentAlias(alias: String)

        fun enlistCategories(categories: List<SubcategoryAttributedCategory>)

    }

    interface Presenter {

        fun takeView(view: View)

        fun onViewCreatedForRoot()
        fun onViewCreatedForSubCategories(alias: String)
        fun onViewDestroyed()

    }

}