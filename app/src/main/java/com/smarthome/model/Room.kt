package com.smarthome.model


import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import com.smarthome.utils.asSequence
import org.json.JSONObject
import java.io.Serializable
import java.nio.charset.Charset

data class Room(val fixture: MutableMap<String, Boolean>) : Serializable {
    companion object {
        fun init(json: JSONObject): Room {
            val result: MutableMap<String, Boolean> = mutableMapOf()
            val array = json.getJSONArray("fixtures")
            for (i in 0..(array.length() - 1)) {
                result.put(array.get(i).toString(), false)
            }
            return Room(result)
        }
    }

    class Deserializer : Deserializable<Rooms> {
        override fun deserialize(response: Response): Rooms {
            val json = Json(response.data.toString(Charset.defaultCharset()))
            return Rooms.init(json.obj())
        }
    }

    class ListDeserializer : Deserializable<MutableList<Rooms>> {
        override fun deserialize(response: Response): MutableList<Rooms> {
            val json = Json(response.data.toString(Charset.defaultCharset()))
            return json.array().asSequence().map { Rooms.init(it as JSONObject) }.toMutableList()
        }
    }
}