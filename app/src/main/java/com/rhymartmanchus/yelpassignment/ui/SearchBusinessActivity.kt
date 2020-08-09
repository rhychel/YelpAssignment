package com.rhymartmanchus.yelpassignment.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
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
import com.rhymartmanchus.yelpassignment.ui.fragments.CategoriesFragment
import com.rhymartmanchus.yelpassignment.ui.viewmodels.BusinessItemVM
import com.rhymartmanchus.yelpassignment.ui.viewmodels.ProgressItemVM
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.coroutines.*
import java.io.IOException


class SearchBusinessActivity : AppCompatActivity(), SearchBusinessContract.View, OnLocationReceiver {

    private companion object {
        const val LIMIT = 20
    }

    private val binder: ActivitySearchBusinessBinding by lazy {
        ActivitySearchBusinessBinding.inflate(layoutInflater)
    }
    private lateinit var svLocation: SearchView

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
    private val sortingStrategyAdapter =
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
                    presenter.onSortingStrategySelected()
                    popupWindow.dismiss()
                }
            }
        )

    private val businessesAdapter = FlexibleAdapter(mutableListOf<AbstractFlexibleItem<*>>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        setSupportActionBar(binder.tbToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bindListeners()
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {

        binder.rvResults.setHasFixedSize(true)
        binder.rvResults.layoutManager = LinearLayoutManager(this)
        binder.rvResults.adapter = businessesAdapter

        businessesAdapter.setEndlessScrollThreshold(LIMIT)
        businessesAdapter.setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
            override fun noMoreLoad(newItemsSize: Int) {
            }

            override fun onLoadMore(lastPosition: Int, currentPage: Int) {
                if(lastPosition % LIMIT == 0)
                    presenter.onLoadMoreBusinesses()
                else
                    stopEndlessScrolling()
            }

        }, ProgressItemVM())
    }

    private fun bindListeners() {
        binder.etKeyword.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                presenter.onSearchClicked()
            }
            true
        }
        binder.etKeyword.addTextChangedListener {
            presenter.takeSearchKeyword(it?.toString()?.takeIf { str -> str.isNotEmpty() })
        }
        binder.ibtnSearch.setOnClickListener {
            hideKeyboard()
            presenter.onSearchClicked()
        }
        binder.ibtnSortBy.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.popup_window_sorting, null, false)

            val rvSortingOptions = view.findViewById<RecyclerView>(R.id.rvSortingOptions).apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@SearchBusinessActivity)
            }
            rvSortingOptions.adapter = sortingStrategyAdapter

            popupWindow.contentView = view
            popupWindow.height = 350
            popupWindow.width = 400
            popupWindow.isOutsideTouchable = true
            popupWindow.showAsDropDown(it, 0, -98)
        }
        binder.btnCategory.setOnClickListener {
            presenter.onCategoriesClicked()
        }
        businessesAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { _, position ->
            val business = businessesAdapter.getItem(position)!! as Business
            presenter.onBusinessClicked(business)
            true
        }
    }

    fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        binder.etKeyword.clearFocus()
        svLocation.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_business, menu)

        svLocation = menu.getItem(0).actionView as SearchView
        svLocation.setIconifiedByDefault(false)
        svLocation.queryHint = "Current location"
        val searchIcon: ImageView = svLocation.findViewById(androidx.appcompat.R.id.search_mag_icon)
        val searchText: SearchView.SearchAutoComplete = svLocation.findViewById(androidx.appcompat.R.id.search_src_text)
        val searchClose: ImageView = svLocation.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchIcon.setImageResource(R.drawable.ic_location)
        searchText.setHintTextColor(ContextCompat.getColor(this, android.R.color.white))
        searchText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        searchClose.setImageResource(R.drawable.ic_close)

        svLocation.setOnCloseListener {
            hideKeyboard()
            presenter.onSearchClicked()

            true
        }
        svLocation.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                presenter.onSearchClicked()
                return true
            }

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
                    presenter.onSearchClicked()
                    hideKeyboard()
                    return true
                }
                hasLocationQuery = true
                job?.cancel()
                job = scope.launch {
                    delay(100L)
                    try {
                        val result = withContext(Dispatchers.IO) { geocoder.getFromLocationName(newText, 1) }
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

    override fun showSortingButton() {
        binder.ibtnSortBy.visibility = View.VISIBLE
    }

    override fun hideSortingButton() {
        binder.ibtnSortBy.visibility = View.GONE
    }

    override fun enlistResults(businesses: List<Business>) {
        businessesAdapter.updateDataSet(businesses.map {
            BusinessItemVM(it)
        })
    }

    override fun clearResults() {
        businessesAdapter.clear()
    }

    override fun appendResults(businesses: List<Business>) {
        businessesAdapter.onLoadMoreComplete(businesses.map {
            BusinessItemVM(it)
        })
    }

    override fun stopEndlessScrolling() {
        businessesAdapter.onLoadMoreComplete(null)
    }

    override fun showProgressItem() {
        businessesAdapter.addItem(
            ProgressItemVM()
        )
    }

    override fun proceedToCategories(category: Category?) {
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    override fun proceedToBusinessDetails(business: Business) {
        val intent = Intent(this, BusinessDetailsActivity::class.java)
        intent.putExtra("alias", business.alias)
        intent.putExtra("name", business.name)
        startActivity(intent)
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

                presenter.onCategorySelected()
            }
        }
    }

}