package com.base.captain.adapter

import android.text.Html
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.base.captain.setCircle
import com.base.captain.setImgUrl

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setText(@IdRes id: Int, text: CharSequence): BaseViewHolder {
        itemView.findViewById<TextView>(id)?.text = text
        return this
    }

    fun setTextColor(@IdRes id: Int, color: Int): BaseViewHolder {
        itemView.findViewById<TextView>(id)?.setTextColor(color)
        return this
    }

    fun setTextHtml(@IdRes id: Int, text: String): BaseViewHolder {
        itemView.findViewById<TextView>(id)?.text = Html.fromHtml(text)
        return this
    }

    fun setBackgroundColor(@IdRes id: Int, color: Int): BaseViewHolder {
        itemView.findViewById<View>(id)?.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(@IdRes id: Int, res: Int): BaseViewHolder {
        itemView.findViewById<TextView>(id)?.setBackgroundResource(res)
        return this
    }

    fun setCheck(@IdRes id: Int, check: Boolean): BaseViewHolder {
        itemView.findViewById<CheckBox>(id)?.isChecked = check
        return this
    }

    fun setImage(@IdRes id: Int, url: String?): BaseViewHolder {
        itemView.findViewById<ImageView>(id)?.setImgUrl(url)
        return this
    }

    fun setImageCircle(@IdRes id: Int, url: String?): BaseViewHolder {
        itemView.findViewById<ImageView>(id)?.setCircle(url, 0)
        return this
    }

    fun setVisiable(@IdRes id: Int, visible: Boolean): BaseViewHolder {
        itemView.findViewById<View>(id)?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setOnClick(@IdRes id: Int, action: (View) -> Unit): BaseViewHolder {
        itemView.findViewById<View>(id)?.setOnClickListener { action(it) }
        return this
    }

    fun setImgRes(@IdRes id: Int, @DrawableRes drawId: Int): BaseViewHolder {
        itemView.findViewById<ImageView>(id)?.setImageResource(drawId)
        return this
    }
}