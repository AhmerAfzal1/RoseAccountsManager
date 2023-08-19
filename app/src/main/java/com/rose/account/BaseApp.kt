package com.rose.account

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        /*
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val mFirebaseAppCheck = FirebaseAppCheck.getInstance()
        mFirebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        Utils.init(this)
        if (NetworkUtils.isConnected()) {
            MobileAds.initialize(this) {
                Log.v(Constants.LOG_TAG, "AdMob Sdk Initialize")
            }
        }
        */
    }
}