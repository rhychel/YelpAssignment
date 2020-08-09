package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutOpenhourItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.OperatingHour
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class OpenHoursVM (
    private val operatingHour: OperatingHour
) : BaseVM<OpenHoursVM.OpenHoursVH>() {

    inner class OpenHoursVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutOpenhourItemBinding by lazy {
            LayoutOpenhourItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: OpenHoursVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvDay.text = operatingHour.day.label
        holder.binder.tvOperatingHours.text = String.format(
            holder.itemView.resources.getString(R.string.open_hours),
            operatingHour.start,
            operatingHour.end
        )

        holder.binder.tvOvernight.visibility = View.GONE
        if(operatingHour.isOvernight)
            holder.binder.tvOvernight.visibility = View.VISIBLE

    }

    override fun equals(other: Any?): Boolean =
        operatingHour.day.label == (other as OperatingHour).day.label

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): OpenHoursVH = OpenHoursVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_openhour_item

}