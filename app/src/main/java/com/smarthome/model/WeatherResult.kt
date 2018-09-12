package com.smarthome.model

import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import org.json.JSONObject
import java.io.Serializable
import java.nio.charset.Charset
import java.util.*

data class WeatherResult(val temperature: String){
    companion object {
        fun init(json: JSONObject): WeatherResult {
            val weatherObj = json.getJSONArray("consolidated_weather")
            if(weatherObj != null && weatherObj.length() > 0){
                return WeatherResult(weatherObj.getJSONObject(0).getDouble("the_temp").toString())
            } else{
                return WeatherResult("25")
            }
        }
    }

    class Deserializer : Deserializable<WeatherResult> {
        override fun deserialize(response: Response): WeatherResult {
            val json = Json(response.data.toString(Charset.defaultCharset()))
            return WeatherResult.init(json.obj())
        }
    }
}
