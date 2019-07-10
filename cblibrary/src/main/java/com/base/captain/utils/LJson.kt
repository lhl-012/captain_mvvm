package com.base.captain.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object LJson {
    val gson: Gson by lazy {
        GsonBuilder().create()
    }

    inline fun <reified T> toJson(t: T) = gson.toJson(t)

    inline fun <reified T> fromJson(json: String) = gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
}
