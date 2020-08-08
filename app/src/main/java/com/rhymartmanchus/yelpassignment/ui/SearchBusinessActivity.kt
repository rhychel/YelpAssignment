package com.rhymartmanchus.yelpassignment.ui

import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.ActivitySearchBusinessBinding

class SearchBusinessActivity : AppCompatActivity() {

    private val binder: ActivitySearchBusinessBinding by lazy {
        ActivitySearchBusinessBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        setSupportActionBar(binder.tbToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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

        return super.onCreateOptionsMenu(menu)
    }

}