package com.rhymartmanchus.yelpassignment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutSortingItemBinding
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy

class SortingStrategyAdapter (
    private val sortingStrategies: List<SortingStrategy>,
    private val onSortingStrategySelected: OnSortingStrategySelected
) : RecyclerView.Adapter<SortingStrategyAdapter.ViewHolder>() {

    interface OnSortingStrategySelected {
        fun onSelected(strategy: SortingStrategy)
    }

    private var selected = 0

    inner class ViewHolder (
        root: View
    ) : RecyclerView.ViewHolder(root) {

        val binder: LayoutSortingItemBinding by lazy {
            LayoutSortingItemBinding.bind(root)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_sorting_item, parent, false)
        )

    override fun getItemCount(): Int = sortingStrategies.size

    private fun SortingStrategy.getLabel(): String =
        when(this) {
            SortingStrategy.Default -> "Recommended"
            SortingStrategy.Rating -> "Rating"
            SortingStrategy.Distance -> "Distance"
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binder.rbSortBy.isChecked = selected == position
        holder.binder.rbSortBy.text = sortingStrategies[position].getLabel()
        holder.binder.rbSortBy.tag = position
        holder.binder.rbSortBy.setOnClickListener {
            val vPosition = it.tag as Int
            onSortingStrategySelected.onSelected(sortingStrategies[vPosition])
            selected = vPosition
            notifyDataSetChanged()
        }

    }

}