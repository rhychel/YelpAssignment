package com.rhymartmanchus.yelpassignment.ui.viewmodels

import android.animation.Animator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.LayoutProgressItemBinding
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.Payload
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ProgressItemVM : BaseVM<ProgressItemVM.ProgressItemVH>() {

    private var status: StatusEnum = StatusEnum.MORE_TO_LOAD

    inner class ProgressItemVH (
        view: View,
        adapter: FlexibleAdapter<*>
    ) : FlexibleViewHolder (view, adapter) {

        val binder: LayoutProgressItemBinding by lazy {
            LayoutProgressItemBinding.bind(view)
        }
        override fun scrollAnimators(
            animators: MutableList<Animator>,
            position: Int,
            isForward: Boolean
        ) {
            AnimatorHelper.scaleAnimator(animators, itemView, 0f)
        }

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: ProgressItemVH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.binder.pbLoader.visibility = View.GONE

        if (payloads.contains(Payload.NO_MORE_LOAD))
            status = StatusEnum.NO_MORE_LOAD

        if(status == StatusEnum.MORE_TO_LOAD)
            holder.binder.pbLoader.visibility = View.VISIBLE

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProgressItemVM

        return true

    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ProgressItemVH = ProgressItemVH(view, adapter)

    override fun getLayoutRes(): Int = R.layout.layout_progress_item

    enum class StatusEnum {
        MORE_TO_LOAD, //Default = should have an empty Action
        NO_MORE_LOAD //Non-empty Action = Action.NO_MORE_LOAD
    }

}