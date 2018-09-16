package com.smarthome.model

import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import com.smarthome.utils.asSequence
import org.json.JSONObject
import java.io.Serializable
import java.nio.charset.Charset

open class Rooms(val bedRoom: Room, val livingRoom: Room, val kitchen: Room): Serializable {
    companion object {
        fun init(json: JSONObject): Rooms {
            val roomsObj = json.getJSONObject("rooms")
            return Rooms(Room.init(roomsObj.getJSONObject("Bedroom")),
                    Room.init(roomsObj.getJSONObject("Living Room")),
                    Room.init(roomsObj.getJSONObject("Kitchen")))
        }
    }

    class Deserializer : Deserializable<Rooms> {
        override fun deserialize(response: Response): Rooms {
            val json = Json(response.data.toString(Charset.defaultCharset()))
            return Rooms.init(json.obj())
        }
    }
}