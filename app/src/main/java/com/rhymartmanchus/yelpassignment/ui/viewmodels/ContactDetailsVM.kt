package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutContactdetailsItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.ContactDetails
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ContactDetailsVM (
    private val contactDetails: ContactDetails,
    private val callNowClicked: () -> Unit
) : BaseBusinessDetailsVM<ContactDetailsVM.ContactDetailsVH>() {

    inner class ContactDetailsVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutContactdetailsItemBinding by lazy {
            LayoutContactdetailsItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: ContactDetailsVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvPhoneNumber.text = contactDetails.displayPhoneNumber

        holder.binder.btnCallNow.setOnClickListener {
            callNowClicked()
        }

    }

    override fun equals(other: Any?): Boolean =
        contactDetails.phoneNumber == (other as ContactDetails).phoneNumber

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ContactDetailsVH = ContactDetailsVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_contactdetails_item

}