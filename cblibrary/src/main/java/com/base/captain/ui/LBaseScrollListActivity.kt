package com.base.captain.ui
import android.view.ViewGroup
import com.base.captain.R
import kotlinx.android.synthetic.main.view_staticky_list.*

/**
 * 列表基类
 */
abstract class LBaseScrollListActivity<D> : LBaseListActivity<D>() {

    override fun setLayout() = R.layout.view_staticky_list
    abstract fun initScrollView():List<Int>
    override fun initView() {
        super.initView()
        if (initScrollView().isNotEmpty()) {
            for (layRes in initScrollView()) {
                addTopView(layRes,llScrollableView)
            }
        }
        refreshLayout.post {
            val height=refreshLayout.measuredHeight
            val barHeight=llScrollableView.measuredHeight
            recyclerView.minimumHeight=height-barHeight
            recyclerView.isNestedScrollingEnabled=false
        }
    }
    open fun getStickyScrollView()=stickyScrollView
    private fun addTopView(headerRes: Int,parentView:ViewGroup) {
        if (headerRes != 0) {
            layoutInflater.inflate(headerRes, parentView, true)
        }
    }
}

