package com.base.captain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log.e
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.base.captain.ui.LBaseActivity
import com.base.captain.ui.LBaseFragment
import com.base.captain.utils.BaseViewModel
import com.base.captain.utils.LJson
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


fun ImageView.setImgUrl(url: String?) {
    //.placeholder(R.drawable.place_pro).error(R.drawable.place_pro)
    Glide.with(this).load(url).into(this)
}

fun ImageView.setImgFile(file: File) {
    //.placeholder(R.drawable.place_pro).error(R.drawable.place_pro)
    Glide.with(this).load(file).apply(RequestOptions().transform(CenterCrop(), CircleCrop())).into(this)
}

fun ImageView.setImgFitxy(url: String?) {
    //.placeholder(R.drawable.place_pro).error(R.drawable.place_pro)
    Glide.with(this).load(url).apply(RequestOptions().skipMemoryCache(true)).into(this)
}

fun ImageView.setCircle(url: String?, error: Int) {
    Glide.with(this).load(url).apply(RequestOptions().error(error).transform(CenterCrop(), CircleCrop())).into(this)
}

fun ImageView.setImgFitxy(url: Int) {
    //.placeholder(R.drawable.place_pro).error(R.drawable.place_pro)
    Glide.with(this).load(url).apply(RequestOptions().skipMemoryCache(true)).into(this)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

fun View.hideKeyBoard() {
    val inputManager = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun ImageView.setImageUrlLoading(url: String?, lp: LinearLayout.LayoutParams, action: () -> Unit) {
    Glide.with(context).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            lp.width = resource.width
            lp.height = resource.height
            this@setImageUrlLoading.layoutParams = lp
            Glide.with(this@setImageUrlLoading).load(resource).into(this@setImageUrlLoading)
            action()
        }
    })
}

fun buildURL(url: String, params: Array<out Pair<String, Any?>>, type:HttpType): String {
    val sb = StringBuilder()
    if (!url.startsWith("http")) {
        sb.append(App.IP)
    }
    sb.append(url)
    if (params.isNotEmpty() && type==HttpType.GET) {
        if (!url.contains("?")) {
            sb.append("?")
        }
        params.forEach {
            sb.append("&").append(it.first).append("=").append(it.second)
        }
    }
    return sb.toString().replace("?&", "?")
}

fun buildPostBody(params: Array<out Pair<String, Any?>>): RequestBody {
    return if (params.isEmpty()) {
        FormBody.Builder().build()
    } else {
        MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            params.forEach {
                when (it.second) {
                    is File ->{
                        val file=it.second as File
                        addFormDataPart(it.first, file.name, file.asRequestBody("image/*".toMediaType()))
                    }
                    else -> addFormDataPart(it.first, it.second.toString())
                }
            }
        }.build()
    }
}

fun String.request(type: HttpType = HttpType.GET,vararg params: Pair<String, Any?>): String? {
    return LHttp.client.newCall(Request.Builder().url(buildURL(this, params, type)).apply {
        if (type==HttpType.POST) post(buildPostBody(params))
    }.build()).execute().body?.string()
}
enum class HttpType {
    GET, POST
}
inline fun <reified T> String.readJsonVM(type: HttpType = HttpType.GET, params: Array<out Pair<String, Any?>>):T?{
    return try {
        LJson.gson.fromJson<T>(LHttp.client.newCall(Request.Builder().url(buildURL(this, params, type)).apply { if (type==HttpType.POST) post(buildPostBody(params)) }.build()).execute().body?.string(), object : TypeToken<T>() {}.type)
    }catch (e: IOException) {
        e("net--","请求错误-->${e.localizedMessage}")
        null
    } catch (e: JsonSyntaxException) {
        e("net--","解析失败-->${e.localizedMessage}")
        null
    }
}
inline fun <reified T> String.readJson(type: HttpType = HttpType.GET, vararg params: Pair<String, Any?>):T?{
    return try {
        LJson.gson.fromJson<T>(LHttp.client.newCall(Request.Builder().url(buildURL(this, params, type)).apply { if (type==HttpType.POST) post(buildPostBody(params)) }.build()).execute().body?.string(), object : TypeToken<T>() {}.type)
    }catch (e: IOException) {
        e("net--","请求错误-->${e.localizedMessage}")
        null
    } catch (e: JsonSyntaxException) {
        e("net--","解析失败-->${e.localizedMessage}")
        null
    }
}

inline fun <reified T : BaseViewModel> LBaseActivity.getViewModel(): T {
    return ViewModelProvider.NewInstanceFactory().create(T::class.java)
}

inline fun <reified T : BaseViewModel> LBaseFragment.getViewModel(): T {
    return ViewModelProvider.NewInstanceFactory().create(T::class.java)
}

fun Context.getString(key:String)=this.getString(this.resources.getIdentifier(key,"string",this.packageName)) ?: ""
