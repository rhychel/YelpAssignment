package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutRatingItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.Rating
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class RatingVM (
    private val rating: Rating
) : BaseVM<RatingVM.RatingVH>() {

    inner class RatingVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutRatingItemBinding by lazy {
            LayoutRatingItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: RatingVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvReviews.text = rating.snippet
        holder.binder.ivRating.setImageResource(RatingAssetProvider.getRatingImage(rating.ratingValue))

    }

    override fun equals(other: Any?): Boolean =
        rating.snippet == (other as Rating).snippet &&
            rating.ratingValue == other.ratingValue

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): RatingVH = RatingVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_rating_item

}