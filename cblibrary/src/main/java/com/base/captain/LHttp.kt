package com.base.captain

import okhttp3.OkHttpClient
import java.net.Proxy
import java.util.concurrent.TimeUnit


object LHttp {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (!App.debug) {
                    this.proxy(Proxy.NO_PROXY)
                }
            }
            .build()
    }

    fun buildUrl(url: String) = if (url.startsWith("http://") || url.startsWith("https://")) url else "${App.IP}$url"
}