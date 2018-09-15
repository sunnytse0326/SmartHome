package com.smarthome.utils

import android.content.Context
import com.smarthome.model.Cache
import com.smarthome.repository.BaseRepository
import java.util.*

class NetworkCache(mContext: Context) {
    private val context: Context = mContext

    fun get(key: String): Object? {
        val data: Cache? = if (Memory(context).getObjectData(key) != null) (Memory(context).getObjectData(key) as Cache) else Cache(null, 0)
        return if (Date().time - (data?.time ?: 0) < BaseRepository.CACHETIME) data?.data else null
    }

    fun set(key: String, data: Object) {
        val cache = Cache(data, Date().time)
        Memory(context).saveObjectData(key, cache)
    }
}