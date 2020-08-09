package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutOpenhourItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.OperatingHour
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class OpenHoursHeaderVM : BaseBusinessDetailsVM<OpenHoursHeaderVM.OpenHoursHeaderVH>() {

    inner class OpenHoursHeaderVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutOpenhourItemBinding by lazy {
            LayoutOpenhourItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: OpenHoursHeaderVH,
        position: Int,
        payloads: MutableList<Any>?
    ) { }

    override fun equals(other: Any?): Boolean = true

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): OpenHoursHeaderVH = OpenHoursHeaderVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_openhour_header

}