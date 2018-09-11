package com.smarthome.repository

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import com.smarthome.model.Rooms
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomRepository: BaseRepository() {
    companion object {
        val FETCH_ROOM = "rooms"
    }

    fun fetchRooms(): Single<Result<Rooms, FuelError>>? = Fuel.get("/$FETCH_ROOM").rx_object(Rooms.Deserializer()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}