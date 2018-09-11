package com.smarthome.application

import android.app.Application
import android.content.Context

class SmartHomeApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }
}