package com.rhymartmanchus.yelpassignment.ui.fragments

import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

interface CategoriesContract {

    interface View {

        fun enlistCategories(categories: List<SubcategoryAttributedCategory>)

    }

    interface Presenter {

        fun takeView(view: View)

        fun onViewCreated()
        fun onViewDestroyed()

    }

}