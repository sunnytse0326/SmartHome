package com.smarthome.jobscheduler

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.smarthome.repository.RoomRepository
import java.util.concurrent.TimeUnit


class SyncWeatherJob : Job() {
    private val roomRepository: RoomRepository = RoomRepository()

    override fun onRunJob(params: Params): Result {

        roomRepository.getWeatherResult()?.subscribe{ result, _ ->
            val (data, err) = result
            val temp:Float = data?.temperature?.toFloat()?:25F
            if(temp > 25F){
                roomRepository.controlFixture("/bedroom/ac/on")
            } else {
                roomRepository.controlFixture("/bedroom/ac/off")
            }
        }

        return Result.SUCCESS
    }
    companion object {
        val TAG = "weather_tag"
        fun scheduleAdvancedJob() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                    .build()
                    .schedule()
        }
    }
}