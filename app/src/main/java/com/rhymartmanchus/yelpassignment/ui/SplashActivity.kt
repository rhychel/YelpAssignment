package com.rhymartmanchus.yelpassignment.ui

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.edwnmrtnz.betterpermission.BetterPermission
import com.github.edwnmrtnz.betterpermission.PermissionCallback
import com.rhymartmanchus.yelpassignment.InstanceProvider
import com.rhymartmanchus.yelpassignment.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity(), SplashContract.View {

    private val binder: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private var betterPermission: BetterPermission? = null
    private val progressDialog: ProgressDialog? = null

    private val presenter: SplashContract.Presenter by lazy {
        SplashPresenter(
            InstanceProvider.appCoroutinesDispatcher,
            this,
            InstanceProvider.fetchCategoriesByLocaleUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        initializeBetterPermission()

    }

    private fun startLoader() {
        Thread(Runnable {
            runOnUiThread {
                Thread.sleep(1000)
                binder.pbLoader.visibility = View.VISIBLE
            }
        }).start()
    }

    private fun initializeBetterPermission() {
        betterPermission = BetterPermission(this)
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .requestPermissions(object : PermissionCallback {
                override fun allPermissionsAreAlreadyGranted() {
                    startLoader()
                    presenter.onLocationIsAllowed()
                }

                override fun onPermissionsGranted() {
                    startLoader()
                    presenter.onLocationIsAllowed()
                }

                override fun onIndividualPermissions(
                    strings: Array<String>,
                    strings1: Array<String>
                ) {
                }

                override fun onPermissionsDeclined() {
                    progressDialog?.dismiss()
                    AlertDialog.Builder(this@SplashActivity)
                        .setCancelable(false)
                        .setTitle("Ooops!")
                        .setMessage("This location permission is required to run this app")
                        .setPositiveButton("Ok") { _, _ -> presenter.onExitClicked() }
                        .show()
                }
            })
        betterPermission?.execute()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        betterPermission!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun popupNetworkFailedDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Ooops!")
            .setMessage("Please check your internet connectivity then tap retry")
            .setPositiveButton("Retry") { _,_ -> presenter.onRetryClicked() }
            .setNegativeButton("Exit") { _,_ -> presenter.onExitClicked() }
            .show()
    }

    override fun proceedToNext() {
        finish()
        startActivity(Intent(this, SearchBusinessActivity::class.java))
    }

    override fun closeView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

}