package com.smarthome

import java.util.HashMap

class SmartHomePageRoute {
    private val pageElementMapping = object : HashMap<String, Int>() {
        init {
            put("Home Page", R.string.app_name)
        }
    }

    fun getPageElementId(name: String): Int {
        return pageElementMapping.get(name) ?: 0
    }
}
