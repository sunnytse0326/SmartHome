package com.smarthome.viewmodel

import android.arch.lifecycle.*
import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.core.FuelError
import com.smarthome.model.FixtureControl
import com.smarthome.model.Room
import com.smarthome.model.Rooms
import com.smarthome.repository.BaseRepository
import com.smarthome.repository.RoomRepository
import com.smarthome.utils.Memory
import com.smarthome.utils.NetworkCache
import java.util.*

class HomeViewModel(val lifecycle: Lifecycle, private val lifecycleOwner: LifecycleOwner, mContext: Context): ViewModel() {
    private val rooms: MutableLiveData<Rooms> = MutableLiveData()
    private val error: MutableLiveData<FuelError> = MutableLiveData()
    private val fixtureControl: MutableLiveData<FixtureControl> = MutableLiveData()
    private val roomRepository: RoomRepository = RoomRepository()
    private val context = mContext

    fun getRoomFixtures(): MutableLiveData<Rooms> {
        roomRepository.fetchRooms()?.subscribe{ result, _ ->
            val (data, err) = result
            if(err == null){
                rooms.value = data
                NetworkCache(context).set(RoomRepository.FETCH_ROOM, data as Object)
            } else {
                val cache = NetworkCache(context).get(RoomRepository.FETCH_ROOM)
                if(cache != null){
                    rooms.value = cache as Rooms
                } else{
                    error.value = err
                }
            }
        }
        return rooms
    }

    fun controlRoomFixtures(url: String):MutableLiveData<FixtureControl> {
        roomRepository.controlFixture(url)?.subscribe { result, _ ->
            val (data, err) = result
            if(err == null){
                fixtureControl?.value = data
            } else{
                error.value = err
            }
        }
        return fixtureControl
    }

    fun getErrors(): MutableLiveData<FuelError> {
        return error
    }

    class VMFactory(private val lifecycle: Lifecycle, private val lifecycleOwner: LifecycleOwner, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(lifecycle, lifecycleOwner, context) as T
    }
}