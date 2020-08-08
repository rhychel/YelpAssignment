package com.rhymartmanchus.yelpassignment.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rhymartmanchus.yelpassignment.databinding.ActivitySearchBusinessBinding

class SearchBusinessActivity : AppCompatActivity() {

    private val binder: ActivitySearchBusinessBinding by lazy {
        ActivitySearchBusinessBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)
    }

}