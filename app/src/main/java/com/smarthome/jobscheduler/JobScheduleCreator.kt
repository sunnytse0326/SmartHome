package com.smarthome.jobscheduler

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


class JobScheduleCreator : JobCreator {
    override fun create(tag: String): Job? {
        when (tag) {
            SyncWeatherJob.TAG -> return SyncWeatherJob()
            else -> return null
        }
    }
}