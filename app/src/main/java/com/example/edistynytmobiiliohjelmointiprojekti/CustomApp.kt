package com.example.edistynytmobiiliohjelmointiprojekti

import android.app.Application
import com.example.edistynytmobiiliohjelmointiprojekti.database.DbProvider

class CustomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DbProvider.provide(this)
    }
}