package com.base.captain.utils

import android.content.Context
import android.content.SharedPreferences

object LSPUtils {
    lateinit var prefs: SharedPreferences
    fun init(context: Context) {
        prefs = context.getSharedPreferences("abc", Context.MODE_PRIVATE)
    }

    fun get(name: String)= get(name,"")

    fun <A> get(name: String, default: A): A = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> getString(name, "")
        }
        return@with res as A
    }

    fun <A> put(name: String, value: A) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, "")
        }.apply()
    }

    /**
     * 删除全部数据
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    fun contain(key: String) = prefs.contains(key)

    /**
     * 根据key删除存储数据
     */
    fun clear(key: String) {
        prefs.edit().remove(key).apply()
    }
}