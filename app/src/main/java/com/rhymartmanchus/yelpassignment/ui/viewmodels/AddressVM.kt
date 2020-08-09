package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutAddressItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.Address
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class AddressVM (
    private val address: Address,
    private val showInMapClicked: () -> Unit
) : BaseBusinessDetailsVM<AddressVM.AddressVH>() {

    inner class AddressVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutAddressItemBinding by lazy {
            LayoutAddressItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: AddressVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvAddress.text = address.fullAddress

        if(address.latitude == null)
            holder.binder.btnShowInMap.visibility = View.INVISIBLE
        else {
            holder.binder.btnShowInMap.setOnClickListener {
                showInMapClicked()
            }
        }

    }

    override fun equals(other: Any?): Boolean =
        address.latitude == (other as Address).latitude &&
            address.longitude == (other as Address).longitude

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): AddressVH = AddressVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_address_item

}