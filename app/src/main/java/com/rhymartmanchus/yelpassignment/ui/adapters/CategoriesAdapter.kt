package com.rhymartmanchus.yelpassignment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutCategoryItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory

class CategoriesAdapter (
    private val categories: MutableList<SubcategoryAttributedCategory>,
    private val onCategorySelectedListener: OnCategorySelectedListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryVH>() {

    interface OnCategorySelectedListener {
        fun onSelected(subcategoryAttributedCategory: SubcategoryAttributedCategory)
    }

    inner class CategoryVH (
        val view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val binder: LayoutCategoryItemBinding by lazy {
            LayoutCategoryItemBinding.bind(view)
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onCategorySelectedListener.onSelected(categories[layoutPosition])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH =
        CategoryVH(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_category_item, parent, false)
        )

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val category = categories[position]
        holder.binder.ivSelected.visibility = View.INVISIBLE
        if(category.subcategories.isNotEmpty())
            holder.binder.ivSelected.visibility = View.VISIBLE
        holder.binder.tvCategory.text = category.title
    }

    fun addAll(categories: List<SubcategoryAttributedCategory>) {
        this.categories.addAll(categories)
    }

    fun add(category: SubcategoryAttributedCategory) {
        this.categories.add(category)
    }

}