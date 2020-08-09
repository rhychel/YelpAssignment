package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutReviewItemBinding
import com.rhymartmanchus.yelpassignment.domain.models.Review
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import java.lang.Exception

class ReviewVM (
    private val review: Review
) : BaseVM<ReviewVM.ReviewVH>() {

    inner class ReviewVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutReviewItemBinding by lazy {
            LayoutReviewItemBinding.bind(view)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: ReviewVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder.binder.tvName.text = review.userName
        holder.binder.tvSnippet.text = review.snippet
        holder.binder.tvDatetime.text = review.timeCreated
        holder.binder.ivRating.setImageResource(RatingAssetProvider.getRatingImage(review.rating))
        holder.binder.ivUserPic.post {

            val height = 200.takeIf { holder.binder.ivUserPic.height == 0 } ?: holder.binder.ivUserPic.height
            val width = 200.takeIf { holder.binder.ivUserPic.width == 0 } ?: holder.binder.ivUserPic.width

            if(review.userPicUrl.isNotEmpty()) {
                Picasso.get()
                    .load(review.userPicUrl)
                    .resize(width, height)
                    .config(Bitmap.Config.ARGB_8888)
                    .centerInside()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.binder.ivUserPic, object : Callback {
                        override fun onSuccess() {
                            holder.binder.ivUserPic.clipToOutline = true
                        }

                        override fun onError(e: Exception?) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
        holder.binder.ivUserPic.clipToOutline = true
    }

    override fun equals(other: Any?): Boolean =
        review.timeCreated == (other as Review).timeCreated &&
            review.userName == other.userName

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ReviewVH = ReviewVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_review_item

}