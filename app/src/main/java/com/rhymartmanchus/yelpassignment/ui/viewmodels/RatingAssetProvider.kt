package com.rhymartmanchus.yelpassignment.ui.viewmodels

import androidx.annotation.DrawableRes
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.domain.models.Rating

object RatingAssetProvider {


    @DrawableRes
    fun getRatingImage(ratings: Rating): Int =
        when(ratings.ratingValue) {
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

}