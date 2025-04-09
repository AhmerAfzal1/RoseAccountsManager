package com.ahmer.accounts

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.perf.FirebasePerformance
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initializeFirebase()
    }

    private fun initializeFirebase() {
        // Initialize Firebase App if not already initialized
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        // Enable Firebase Analytics collection
        FirebaseAnalytics.getInstance(this).apply {
            setAnalyticsCollectionEnabled(true)
        }

        // Enable Crashlytics collection
        FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = true
        }

        // Enable Performance Monitoring
        FirebasePerformance.getInstance().apply {
            isPerformanceCollectionEnabled = true
        }

        // Enable Firebase Cloud Messaging auto-init
        FirebaseMessaging.getInstance().apply {
            isAutoInitEnabled = true
        }

        // Set up Firebase App Check with Play Integrity provider
        FirebaseAppCheck.getInstance().apply {
            installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
        }
    }
}