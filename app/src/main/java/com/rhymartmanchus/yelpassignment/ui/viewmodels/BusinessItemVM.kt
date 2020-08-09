package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutBusinessItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class BusinessItemVM (
    val business: Business
) : AbstractFlexibleItem<BusinessItemVM.BusinessItemVH>() {

    inner class BusinessItemVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutBusinessItemBinding by lazy {
            LayoutBusinessItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: BusinessItemVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvBusinessName.text = business.name
        holder.binder.tvReviews.text = business.ratings.snippet
        holder.binder.ivRating.setImageResource(getRatingImage())
        holder.binder.tvBusinessAddress.text = business.address.fullAddress
        holder.binder.ivPhoto.post {

            val height = 200.takeIf { holder.binder.ivPhoto.height == 0 } ?: holder.binder.ivPhoto.height
            val width = 200.takeIf { holder.binder.ivPhoto.width == 0 } ?: holder.binder.ivPhoto.width

            if(business.photoUrl.isNotEmpty()) {
                Picasso.get()
                    .load(business.photoUrl)
                    .resize(width, height)
                    .config(Bitmap.Config.ARGB_8888)
                    .centerInside()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.binder.ivPhoto)
            }
        }

        holder.binder.tvDistance.text = business.distance
        holder.binder.tvOperatingStatus.text = if(business.isClosed) "CLOSED" else "OPEN"
        holder.binder.tvOperatingStatus.setBackgroundResource(if(business.isClosed) R.drawable.bg_closed else R.drawable.bg_opened)

    }

    @DrawableRes
    private fun getRatingImage(): Int =
        when(business.ratings.ratingValue) {
            1.0 -> R.drawable.ic_rating_1
            1.5 -> R.drawable.ic_rating_1_5
            2.0 -> R.drawable.ic_rating_2
            2.5 -> R.drawable.ic_rating_2_5
            3.0 -> R.drawable.ic_rating_3
            3.5 -> R.drawable.ic_rating_3_5
            4.0 -> R.drawable.ic_rating_4
            4.5 -> R.drawable.ic_rating_4_5
            5.0 -> R.drawable.ic_rating_5
            else -> R.drawable.ic_rating_0
        }

    override fun equals(other: Any?): Boolean =
        business.id == (other as Business).id

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): BusinessItemVH = BusinessItemVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_business_item

}