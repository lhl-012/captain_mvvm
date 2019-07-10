package com.base.captain.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.Toast

object LToast {
    lateinit var toast: Toast
    @SuppressLint("ShowToast")
    fun init(context: Context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    fun show(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            toast.setText(text)
            toast.show()
        }
    }
}