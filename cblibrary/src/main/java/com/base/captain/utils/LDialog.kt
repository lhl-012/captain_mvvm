package com.base.captain.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

object LDialog {
    fun customDialog(ctx: Context, view: View, width: Int, flag: Boolean = true) = AlertDialog
        .Builder(ctx)
        .setView(view)
        .create()
        .apply {
            this.setCancelable(flag)
            this.setCanceledOnTouchOutside(flag)
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.show()
            if (width > 0) {
                this.window?.setLayout(dp2px(ctx, width), ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    fun customDialog3(ctx: Context, view: View, width: Int, flag: Boolean = true) = AlertDialog
        .Builder(ctx)
        .setView(view)
        .create()
        .apply {
            this.setCancelable(flag)
            this.setCanceledOnTouchOutside(flag)
            this.window?.setGravity(Gravity.BOTTOM)
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.show()
            if (width > 0) {
                this.window?.setLayout(dp2px(ctx, width), ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }

    fun customDialog2(ctx: Context, view: View, flag: Boolean = true) = AlertDialog
        .Builder(ctx)
        .setView(view)
        .create()
        .apply {
            this.setCancelable(flag)
            this.setCanceledOnTouchOutside(flag)
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    fun dp2px(context: Context, dipValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue.toFloat(), context.resources.displayMetrics
        ).toInt()
    }

    fun showDialog(context: Context, title: String, msg: String, action: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("确定") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showMsgDialog(context: Context, msg: String, btn: String, action: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(btn) { dialog, _ ->
                dialog.dismiss()
                action()
            }
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showNoCancelDialog(context: Context, title: String, msg: String, action: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("确定") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showCancelDialog(context: Context, title: String, msg: String, action: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("确定") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showListDialog(context: Context, title: String, items: Array<String>, action: (Int) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items) { dialog, which ->
                dialog.dismiss()
                action(which)
            }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showInputDialog(context: Context, title: String, place: String, inputType: Int, action: (String) -> Unit) {
        val editText = EditText(context)
        editText.setBackgroundColor(Color.WHITE)
        editText.hint = place
        editText.setPadding(40, 40, 40, 40)
        if (inputType == 0) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        } else editText.inputType = inputType
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("确定") { d, _ ->
                val text = editText.text.toString()
                if (text.isNotBlank()) {
                    action(text)
                    d.dismiss()
                } else {
                    LToast.show("请填入")
                }
            }
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.window?.setLayout(dp2px(context, 350), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}