package com.base.captain.widget

import android.content.Context
import android.util.AttributeSet
import com.scwang.smartrefresh.layout.SmartRefreshLayout

class RefreshAndLoadView(context: Context, attrs: AttributeSet) : SmartRefreshLayout(context, attrs) {
    fun onRefreshListener(action: () -> Unit){
        setOnRefreshListener {
            action()
        }
    }
    fun onLoadMoreListener(action: () -> Unit){
        setOnLoadMoreListener {
            action()
        }
    }
}