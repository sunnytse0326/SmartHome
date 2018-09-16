package com.smarthome

import android.os.Bundle
import android.support.test.runner.MonitoringInstrumentation
import cucumber.api.CucumberOptions
import cucumber.api.android.CucumberInstrumentationCore

@CucumberOptions(features = arrayOf("features"))
class SmartHomeCucumberRunner : MonitoringInstrumentation() {

    private val instrumentationCore = CucumberInstrumentationCore(this)

    override fun onCreate(bundle: Bundle) {
        super.onCreate(bundle)
        instrumentationCore.create(bundle)
        start()
    }

    override fun onStart() {
        waitForIdleSync()
        instrumentationCore.start()
    }
}