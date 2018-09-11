package com.smarthome.viewmodel

import android.arch.lifecycle.*
import android.util.Log
import com.github.kittinunf.fuel.core.FuelError
import com.smarthome.model.Room
import com.smarthome.model.Rooms
import com.smarthome.repository.BaseRepository
import com.smarthome.repository.RoomRepository
import com.smarthome.utils.Memory
import com.smarthome.utils.NetworkCache
import java.util.*

class HomeViewModel(val lifecycle: Lifecycle, private val lifecycleOwner: LifecycleOwner): ViewModel() {
    val rooms: MutableLiveData<Rooms> = MutableLiveData()
    val error: MutableLiveData<FuelError> = MutableLiveData()

    val roomRepository: RoomRepository = RoomRepository()

    fun getRoomFixtures(): MutableLiveData<Rooms> {
        roomRepository.fetchRooms()?.subscribe{ result, _ ->
            val (data, err) = result
            if(err == null){
                rooms.value = data
                NetworkCache.set(RoomRepository.FETCH_ROOM, data as Object)
            } else {
                val cache = NetworkCache.get(RoomRepository.FETCH_ROOM)
                if(cache != null){
                    rooms.value = cache as Rooms
                } else{
                    error.value = err
                }
            }
        }
        return rooms
    }

    fun addPosts(){

    }

    fun getErrors(): MutableLiveData<FuelError> {
        return error
    }

    class VMFactory(private val lifecycle: Lifecycle, private val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(lifecycle, lifecycleOwner) as T
    }
}