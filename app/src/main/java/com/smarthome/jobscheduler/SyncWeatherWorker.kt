package com.smarthome.jobscheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import androidx.work.*
import com.smarthome.repository.RoomRepository
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class SyncWeatherWorker : Worker() {
    private val roomRepository: RoomRepository = RoomRepository()
    private val WEATHER_CHANNEL_ID = "weather_channel_id"
    private val WEATHER_CHANNEL_NAME = "weather_channel_name"
    private var periodicStart = false

    override fun doWork(): Result {
        fetchWeatherResult()
        return Result.SUCCESS
    }

    private fun fetchWeatherResult() {
        val weatherThread = CountDownLatch(1)
        if (this.tags.contains(TAG_PERIODIC) && !periodicStart) {
            periodicStart = true
        } else {
            roomRepository.getWeatherResult()?.subscribe { result, _ ->
                val (data, err) = result
                val temp: Float = data?.temperature?.toFloat() ?: 25F
                if (temp > 25F) {
                    roomRepository.controlFixture("/bedroom/ac/on")?.subscribe { result, _ ->
                        val (data, err) = result
                        if (err == null) {
                            outputData = Data.Builder().putBooleanArray(RESULT_DATA, booleanArrayOf(true, true)).build()
                            setPushNotification("on")
                        } else {
                            outputData = Data.Builder().putBooleanArray(RESULT_DATA, booleanArrayOf(false, false)).build()
                        }
                        weatherThread.countDown()
                    }
                } else {
                    roomRepository.controlFixture("/bedroom/ac/off")?.subscribe { result, _ ->
                        val (data, err) = result
                        if (err == null) {
                            setPushNotification("off")
                            outputData = Data.Builder().putBooleanArray(RESULT_DATA, booleanArrayOf(true, false)).build()
                        } else {
                            outputData = Data.Builder().putBooleanArray(RESULT_DATA, booleanArrayOf(false, false)).build()
                        }
                        weatherThread.countDown()
                    }
                }
            }
            weatherThread.await()
        }
    }

    private fun setPushNotification(action: String) {
        if (this.tags.contains(TAG_PERIODIC)) {
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "weather_notification")
                    .setSmallIcon(com.smarthome.R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, com.smarthome.R.mipmap.ic_launcher))
                    .setContentTitle(applicationContext.getString(com.smarthome.R.string.bedroom))
                    .setContentText(String.format(applicationContext.getString(com.smarthome.R.string.switch_announce), action))

            val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(WEATHER_CHANNEL_ID, WEATHER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(mChannel)
                notificationBuilder.setChannelId(WEATHER_CHANNEL_ID)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    companion object {
        val TAG_SINGLE = "weather_tag_single"
        val TAG_PERIODIC = "weather_tag_periodic"
        val RESULT_DATA = "result_success"

        fun scheduleWeatherJob(): UUID {
            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val build = OneTimeWorkRequest.Builder(SyncWeatherWorker::class.java).setConstraints(constraints).addTag(TAG_SINGLE).build()
            WorkManager.getInstance().enqueue(build)
            return build.id
        }

        fun schedulePeriodicWeatherJob() {
            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val build = PeriodicWorkRequest.Builder(SyncWeatherWorker::class.java, 15, TimeUnit.MINUTES)
                    .addTag(TAG_PERIODIC)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(TAG_PERIODIC, ExistingPeriodicWorkPolicy.REPLACE, build)
        }
    }
}