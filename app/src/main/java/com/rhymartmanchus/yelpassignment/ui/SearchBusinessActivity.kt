package com.rhymartmanchus.yelpassignment.ui

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edwnmrtnz.locationprovider.LocationProviderHelper
import com.edwnmrtnz.locationprovider.callback.OnLocationReceiver
import com.edwnmrtnz.locationprovider.enums.LocationUpdateStatus
import com.rhymartmanchus.yelpassignment.InstanceProvider
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.ActivitySearchBusinessBinding
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.ui.adapters.SortingStrategyAdapter
import kotlinx.coroutines.*
import java.io.IOException


class SearchBusinessActivity : AppCompatActivity(), SearchBusinessContract.View, OnLocationReceiver {

    private val binder: ActivitySearchBusinessBinding by lazy {
        ActivitySearchBusinessBinding.inflate(layoutInflater)
    }

    private var isLocating = false
    private var hasLocationQuery = false
    private lateinit var currentLocation: Location

    private var job: Job? = null
    private val scope by lazy {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    private val geocoder: Geocoder by lazy {
        Geocoder(this)
    }
    private val presenter: SearchBusinessContract.Presenter by lazy {
        SearchBusinessPresenter(
            InstanceProvider.appCoroutinesDispatcher,
            this,
            InstanceProvider.fetchBusinessesByLocationUseCase,
            InstanceProvider.searchBusinessesByKeywordUseCase,
            InstanceProvider.fetchBusinessesInCategoryByLocationUseCase,
            InstanceProvider.searchBusinessesInCategoryByKeywordUseCase
        )
    }
    private val popupWindow by lazy {
        PopupWindow(this)
    }
    private val adapter =
        SortingStrategyAdapter(
            listOf(
                SortingStrategy.Default,
                SortingStrategy.Distance,
                SortingStrategy.Rating
            ),
            object :
                SortingStrategyAdapter.OnSortingStrategySelected {
                override fun onSelected(strategy: SortingStrategy) {
                    presenter.takeSortingStrategy(strategy)
                    popupWindow.dismiss()
                }
            }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        setSupportActionBar(binder.tbToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binder.etKeyword.addTextChangedListener {
            presenter.takeSearchKeyword(it?.toString() ?: "")
        }
        binder.ibtnSearch.setOnClickListener {
            presenter.onSearchClicked()
        }
        binder.ibtnSortBy.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.popup_window_sorting, null, false)

            val rvSortingOptions = view.findViewById<RecyclerView>(R.id.rvSortingOptions).apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@SearchBusinessActivity)
            }
            rvSortingOptions.adapter = adapter

            popupWindow.contentView = view
            popupWindow.height = 500
            popupWindow.width = 400
            popupWindow.isOutsideTouchable = true
            popupWindow.showAsDropDown(it, 0, -98)
        }
        binder.btnCategory.setOnClickListener {
            presenter.onCategoriesClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_business, menu)

        val sv = menu.getItem(0).actionView as SearchView
        sv.setIconifiedByDefault(false)
        sv.queryHint = "Current location"
        val searchIcon: ImageView = sv.findViewById(androidx.appcompat.R.id.search_mag_icon)
        val searchText: SearchView.SearchAutoComplete = sv.findViewById(androidx.appcompat.R.id.search_src_text)
        val searchClose: ImageView = sv.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchIcon.setImageResource(R.drawable.ic_location)
        searchText.setHintTextColor(ContextCompat.getColor(this, android.R.color.white))
        searchText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        searchClose.setImageResource(R.drawable.ic_close)
        sv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()) {
                    hasLocationQuery = false
                    presenter.takeCoordinates(
                        Coordinates(
                            currentLocation.latitude,
                            currentLocation.longitude,
                            newText
                        )
                    )
                    return true
                }
                hasLocationQuery = true
                job?.cancel()
                job = scope.launch {
                    delay(100L)
                    try {
                        val result = geocoder.getFromLocationName(newText, 1)
                        if(result.isNotEmpty()) {
                            presenter.takeCoordinates(
                                Coordinates(
                                    result[0].latitude,
                                    result[0].longitude,
                                    newText
                                )
                            )
                        }
                        else {
                            presenter.setNoCoordinates()
                        }
                    } catch (e: IOException) {
                        presenter.setNoCoordinates()
                    }
                }

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun startLocationUpdates() {
        isLocating = true
        LocationProviderHelper.getInstance(this).startLocationUpdates()
        LocationProviderHelper.getInstance(this).setOnLocationReceiver(this)
    }

    private fun stopLocationUpdates() {
        isLocating = false
        if(isLocating)
            LocationProviderHelper.getInstance(this).stopLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    override fun onLocationAcquired(location: Location, accuracy: Float) {
        currentLocation = location
        if(!hasLocationQuery)
            presenter.takeCoordinates(
                Coordinates(
                    location.latitude,
                    location.longitude,
                    "Current location"
                )
            )
    }

    override fun onResolutionRequired(e: Exception) {
        TODO("Not yet implemented")
    }

    override fun onLocationReceiverStarted() {
        presenter.onGettingLocationStarted()
    }

    override fun onFailed(locationUpdateStatus: LocationUpdateStatus) {
        TODO("Not yet implemented")
    }

    override fun showLoadingGroup() {
        binder.grpLoader.visibility = View.VISIBLE
    }

    override fun hideLoadingGroup() {
        binder.grpLoader.visibility = View.GONE
    }

    override fun showNoResults() {
        binder.tvNoResults.visibility = View.VISIBLE
    }

    override fun hideNoResults() {
        binder.tvNoResults.visibility = View.GONE
    }

    override fun hideSearchInvitation() {
        binder.tvInvitation.visibility = View.GONE
    }

    override fun enlistResults(businesses: List<Business>) {
        Log.e("REsults", businesses.joinToString { it.name })
    }

    override fun proceedToCategories(category: Category?) {
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    override fun setCategoriesButtonText(categoryTitle: String) {
        binder.btnCategory.text = categoryTitle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000) {
            if(resultCode == 200) {
                val category = data?.getParcelableExtra<Category>("category")!!
                if(category.alias == "yelph-all")
                    presenter.invalidateCategory()
                else
                    presenter.takeCategory(category)
            }
        }
    }

}