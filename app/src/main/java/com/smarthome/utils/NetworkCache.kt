package com.smarthome.utils

import com.smarthome.model.Cache
import com.smarthome.repository.BaseRepository
import java.util.*

class NetworkCache {
    companion object {
        fun get(key: String): Object? {
            val data: Cache? = if (Memory.getObjectData(key) != null) (Memory.getObjectData(key) as Cache) else Cache(null,0)
            return if(Date().time - (data?.time?:0) < BaseRepository.CACHETIME) data?.data else null
        }

        fun set(key: String, data: Object) {
            val cache = Cache(data, Date().time)
            Memory.saveObjectData(key, cache)
        }
    }
}