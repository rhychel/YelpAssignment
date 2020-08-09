package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutCategoryItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class CategoryItemVM (
    val subcategoryAttributedCategory: SubcategoryAttributedCategory
) : AbstractFlexibleItem<CategoryItemVM.CategoryItemVH>() {

    inner class CategoryItemVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder(view, adapter) {

        val binder: LayoutCategoryItemBinding by lazy {
            LayoutCategoryItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: CategoryItemVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.ivSelected.visibility = View.INVISIBLE
        if(subcategoryAttributedCategory.subcategories.isNotEmpty())
            holder.binder.ivSelected.visibility = View.VISIBLE
        holder.binder.tvCategory.text = subcategoryAttributedCategory.title

    }

    override fun equals(other: Any?): Boolean =
        subcategoryAttributedCategory.alias == (other as CategoryItemVM).subcategoryAttributedCategory.alias

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): CategoryItemVH = CategoryItemVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_category_item

}