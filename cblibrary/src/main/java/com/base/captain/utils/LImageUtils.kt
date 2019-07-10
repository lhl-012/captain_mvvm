package com.base.captain.utils

import android.app.Activity
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object LImageUtils {
    fun display(imageView: ImageView, bitmap: Bitmap?) {
        Glide.with(imageView.context).load(bitmap).into(imageView)
    }

    fun display(imageView: ImageView, url: String, size: Int) {
        Glide.with(imageView.context).load(url).apply(RequestOptions.centerCropTransform().override(size, size)).into(imageView)
    }

    fun display(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).apply(RequestOptions.centerCropTransform()).into(imageView)
    }

    fun displaycenterInside(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).apply(RequestOptions.centerInsideTransform()).into(imageView)
    }

    fun getBitmap(aty: Activity, url: String) = Glide.with(aty).asBitmap().load(url).submit(120, 120).get()
}