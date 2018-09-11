package com.smarthome.repository

import com.github.kittinunf.fuel.core.FuelManager

open class BaseRepository{
    companion object {
        val CACHETIME = 4 * 60 * 60 * 1000 // 4 hours caching
    }

    init {
        FuelManager.instance.basePath = "http://private-1e863-house4591.apiary-mock.com/"
    }
}