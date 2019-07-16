package com.base.captain.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object LBroadcastUtils {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    fun init(context: Context) {
        localBroadcastManager = LocalBroadcastManager.getInstance(context)
    }

    fun add(receiver: BroadcastReceiver, key: String) {
        localBroadcastManager.registerReceiver(receiver, IntentFilter(key))
    }

    fun send(key: String, value: String) {
        localBroadcastManager.sendBroadcast(Intent().setAction(key).putExtra("value", value))
    }
    fun send(key: String, bundle: Bundle) {
        localBroadcastManager.sendBroadcast(Intent().setAction(key).putExtra("value", bundle))
    }

    fun remove(receiver: BroadcastReceiver) {
        localBroadcastManager.unregisterReceiver(receiver)
    }
}
