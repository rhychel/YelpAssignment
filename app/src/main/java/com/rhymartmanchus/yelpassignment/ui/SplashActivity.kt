package com.rhymartmanchus.yelpassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rhymartmanchus.yelpassignment.R
import com.rhymartmanchus.yelpassignment.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binder: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Thread(Runnable {
            runOnUiThread {
                Thread.sleep(1000)
                binder.pbLoader.visibility = View.VISIBLE
            }
        }).start()

    }
}