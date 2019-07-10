package com.base.captain.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class LLoadingDialog(context: Context, private val msg:String="正在加载...", private val cancelAble: Boolean=false) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER
        linearLayout.setPadding(dp2px(context, 15),dp2px(context, 15),dp2px(context, 15),dp2px(context, 15))
        val progressBar = ProgressBar(context)
        linearLayout.addView(progressBar)
        val progressBarLp = progressBar.layoutParams as LinearLayout.LayoutParams
        progressBarLp.width = dp2px(context, 36)
        progressBarLp.height = dp2px(context, 36)
        progressBar.layoutParams = progressBarLp
        val textView = TextView(context)
        textView.text = msg
        textView.textSize = 12f
        textView.setSingleLine()
        textView.gravity=Gravity.CENTER
        linearLayout.addView(textView)
        val textViewLp = textView.layoutParams as LinearLayout.LayoutParams
        textViewLp.width = dp2px(context, 120)
        textViewLp.topMargin = dp2px(context, 5)
        textView.layoutParams = textViewLp
        setContentView(linearLayout)
        setCancelable(cancelAble)
    }

    override fun show() {
        super.show()
        window?.setLayout(dp2px(context, 120), dp2px(context, 120))
    }

    private fun dp2px(context: Context, dp: Int): Int {
        return (context.resources.displayMetrics.density * dp + 0.5).toInt()
    }
}