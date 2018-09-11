package com.smarthome.utils

import org.json.JSONArray

fun JSONArray.asSequence(): Sequence<Any> = object : Sequence<Any> {
    override fun iterator(): Iterator<Any> = object : Iterator<Any> {
        val iter = (0 until this@asSequence.length()).iterator()
        override fun hasNext(): Boolean = iter.hasNext()
        override fun next(): Any {
            val i = iter.next()
            return this@asSequence.get(i)
        }
    }
}