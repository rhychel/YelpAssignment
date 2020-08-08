package com.rhymartmanchus.yelpassignment.ui

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.edwnmrtnz.locationprovider.LocationProviderHelper
import com.edwnmrtnz.locationprovider.callback.OnLocationReceiver
import com.edwnmrtnz.locationprovider.enums.LocationUpdateStatus
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.ActivitySearchBusinessBinding
import com.rhymartmanchus.yelpassignment.domain.models.Business
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
    private lateinit var presenter: SearchBusinessContract.Presenter

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
                        presenter.takeCoordinates(
                            Coordinates(
                                result[0].latitude,
                                result[0].longitude,
                                newText
                            )
                        )
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
        Log.e("Location", "Coordinates is (${location.latitude}, ${location.longitude})")
        currentLocation = location
//        if(!hasLocationQuery)
//            presenter.takeCoordinates(
//                Coordinates(
//                    location.latitude,
//                    location.longitude,
//                    "Current location"
//                )
//            )
    }

    override fun onResolutionRequired(e: Exception) {
        TODO("Not yet implemented")
    }

    override fun onLocationReceiverStarted() {
//        presenter.onGettingLocationStarted()
    }

    override fun onFailed(locationUpdateStatus: LocationUpdateStatus) {
        TODO("Not yet implemented")
    }

    override fun showLoadingGroup() {
        TODO("Not yet implemented")
    }

    override fun hideLoadingGroup() {
        TODO("Not yet implemented")
    }

    override fun showNoResults() {
        TODO("Not yet implemented")
    }

    override fun hideNoResults() {
        TODO("Not yet implemented")
    }

    override fun enlistResults(businesses: List<Business>) {
        TODO("Not yet implemented")
    }

}