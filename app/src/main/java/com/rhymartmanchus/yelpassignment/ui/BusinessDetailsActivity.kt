package com.rhymartmanchus.yelpassignment.ui

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.yelpassignment.InstanceProvider
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.ActivityBusinessDetailsBinding
import com.rhymartmanchus.yelpassignment.domain.models.Address
import com.rhymartmanchus.yelpassignment.domain.models.ContactDetails
import com.rhymartmanchus.yelpassignment.domain.models.OperatingHour
import com.rhymartmanchus.yelpassignment.domain.models.Rating
import com.rhymartmanchus.yelpassignment.ui.viewmodels.*
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter


class BusinessDetailsActivity : AppCompatActivity(), BusinessDetailsContract.View {

    private val binder: ActivityBusinessDetailsBinding by lazy {
        ActivityBusinessDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var progressDialog: ProgressDialog

    private val presenter: BusinessDetailsContract.Presenter by lazy {
        BusinessDetailsPresenter(
            InstanceProvider.appCoroutinesDispatcher,
            this,
            InstanceProvider.fetchBusinessByAliasUseCase
        )
    }

    private val adapter by lazy {
        FlexibleAdapter<BaseBusinessDetailsVM<*>>(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        setSupportActionBar(binder.tbActionbar)

        binder.rvBusinessDetails.setHasFixedSize(true)
        binder.rvBusinessDetails.layoutManager = LinearLayoutManager(this)
        binder.rvBusinessDetails.adapter = adapter
        binder.rvBusinessDetails.addItemDecoration(OpenHoursDividerItemDecoration())
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter.onViewCreated(
            intent.getStringExtra("name")!!,
            intent.getStringExtra("alias")!!
        )

    }

    private inner class OpenHoursDividerItemDecoration : DividerItemDecoration(this, VERTICAL) {

        fun hideDivider(
            item: BaseBusinessDetailsVM<*>,
            outRect: Rect,
            default: () -> Unit
        ) {

            if(item is OpenHoursVM) {
                outRect.setEmpty()
            } else {
                default()
            }

        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val serviceArea = adapter.getItem(position)!!

            hideDivider(serviceArea, outRect) {
                super.getItemOffsets(outRect, view, parent, state)
            }

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun popupLoadingDialog() {
        progressDialog = ProgressDialog.show(this,
            "Please wait", "Loading ${intent.getStringExtra("name")}...",
            true,
            false)
        progressDialog.show()
    }

    override fun dismissLoadingDialog() {
        progressDialog.dismiss()
    }

    override fun popupNetworkFailureDialog() {
        TODO("Not yet implemented")
    }

    override fun popupBusinessIsMigratedDialog(alias: String) {
        TODO("Not yet implemented")
    }

    override fun popupBusinessNotFoundDialog(name: String) {
        TODO("Not yet implemented")
    }

    override fun showNameAndPhoto(name: String, photoUrl: String) {
        binder.collapsingToolbar.title = name
        binder.ivPhoto.post {

            val height = 200.takeIf { binder.ivPhoto.height == 0 } ?: binder.ivPhoto.height
            val width = 200.takeIf { binder.ivPhoto.width == 0 } ?: binder.ivPhoto.width

            if(photoUrl.isNotEmpty()) {
                Picasso.get()
                    .load(photoUrl)
                    .resize(width, height)
                    .config(Bitmap.Config.ARGB_8888)
                    .centerInside()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binder.ivPhoto)
            }
        }

    }

    override fun showCategories(categories: List<String>) {
        binder.collapsingToolbar.subtitle = categories.joinToString { it }
    }

    override fun showContactDetails(contactDetails: ContactDetails) {
        adapter.addItem(
            ContactDetailsVM(
                contactDetails
            ) {
                presenter.onCallBusinessClicked()
            }
        )
    }

    override fun showAddress(address: Address) {
        adapter.addItem(AddressVM(address) {
            presenter.onShowInMapClicked()
        })
    }

    override fun showOpenHours(operations: List<OperatingHour>) {
        adapter.addItem(
            OpenHoursHeaderVM()
        )
        operations.forEach {
            adapter.addItem(
                OpenHoursVM(it)
            )
        }
    }

    override fun showRating(rating: Rating) {
        TODO("Not yet implemented")
    }

    override fun showReviews() {
        TODO("Not yet implemented")
    }

    override fun callBusiness(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    override fun openMap(latitude: Double, longitude: Double) {
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude (${intent.getStringExtra("name")})"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

}