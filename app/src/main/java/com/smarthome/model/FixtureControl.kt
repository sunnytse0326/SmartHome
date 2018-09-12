package com.smarthome.model

import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import com.smarthome.utils.asSequence
import org.json.JSONObject
import java.io.Serializable
import java.nio.charset.Charset

data class FixtureControl(val control: Boolean): Serializable {
    companion object {
        fun init(result: String): FixtureControl {
            return FixtureControl(result == "true")
        }
    }
    class Deserializer : Deserializable<FixtureControl> {
        override fun deserialize(response: Response): FixtureControl {
            return FixtureControl.init(response.data.toString(Charset.defaultCharset()))
        }
    }
}