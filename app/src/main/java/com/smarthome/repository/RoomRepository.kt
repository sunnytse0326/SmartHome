package com.smarthome.repository

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import com.smarthome.model.FixtureControl
import com.smarthome.model.Rooms
import com.smarthome.model.WeatherResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomRepository: BaseRepository() {
    companion object {
        val FETCH_ROOM = "rooms"
        enum class RoomType(val url: String) {
            BEDROOM("bedroom"),
            LIVING_ROOM("living-room"),
            KITCHEN("kitchen")
        }
    }

    fun fetchRooms(): Single<Result<Rooms, FuelError>>? = Fuel.get("/$FETCH_ROOM").rx_object(Rooms.Deserializer()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

    fun controlFixture(url: String): Single<Result<FixtureControl, FuelError>>? = Fuel.get(url).rx_object(FixtureControl.Deserializer()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

    fun getWeatherResult(): Single<Result<WeatherResult, FuelError>>? = Fuel.get("https://www.metaweather.com/api/location/2165352").rx_object(WeatherResult.Deserializer()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}