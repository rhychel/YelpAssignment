package com.rhymartmanchus.yelpassignment

import android.app.Application

class YelPHApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InstanceProvider.initialize(this)
    }

}