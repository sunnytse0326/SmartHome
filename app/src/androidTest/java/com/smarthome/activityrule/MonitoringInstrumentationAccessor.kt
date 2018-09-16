package com.smarthome.activityrule

import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.MonitoringInstrumentation

class MonitoringInstrumentationAccessor {
    private val handler = Handler(Looper.getMainLooper())

    fun finishAllActivities() {
        val instrumentation = InstrumentationRegistry.getInstrumentation() as MonitoringInstrumentation
        val activityFinisher = instrumentation.ActivityFinisher()
        handler.post(activityFinisher)
    }
}