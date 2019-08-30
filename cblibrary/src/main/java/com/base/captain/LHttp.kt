package com.base.captain

import android.util.Log
import com.base.captain.utils.StringEscapeUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.net.Proxy
import java.util.concurrent.TimeUnit


object LHttp {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (!App.debug) {
                    proxy(Proxy.NO_PROXY)
                } else {
                    addInterceptor(LoggingInterceptor())
                }
            }
            .build()
    }

    internal class LoggingInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            Log.d("vvvv-net-url", request.url.toString())
            val response = chain.proceed(request)
            Log.d(
                "vvvv-net-res",
                StringEscapeUtils.unescapeJava(response.body?.source()?.apply { request(Long.MAX_VALUE) }?.buffer?.clone()?.readUtf8())
            )
            return response
        }
    }

    fun buildUrl(url: String, webIp: String = App.IP) =
        if (url.startsWith("http://") || url.startsWith("https://")) url else "$webIp$url"
}