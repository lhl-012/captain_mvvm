package com.base.captain.ui
import android.view.ViewGroup
import com.base.captain.R
import com.base.captain.adapter.BaseViewHolder
import com.base.captain.adapter.LBaseAdapter
import com.base.captain.utils.then
import kotlinx.android.synthetic.main.activity_coll_list.*

/**
 * 列表基类
 */
abstract class LBaseScrollListFragment<D> : LBaseFragment() {

    enum class Type {
        NONE, TOP, BOTTOM, BOTH
    }

    interface BindAction<D> {
        fun bind(holder: BaseViewHolder, model: D, pos: Int)
    }

    open var nowPage = 1

    override fun setLayout() = R.layout.activity_coll_list
    private val mList: ArrayList<D> by lazy { ArrayList<D>() }
    private val mAdapter: LBaseAdapter<D> by lazy { ListAdp(itemLayout(), mList, action) }
    private lateinit var action: BindAction<D>
    abstract fun initHeader(): List<Int>
    abstract fun initScrollableView():List<Int>
    abstract fun initAlwaysView():List<Int>
    abstract fun itemLayout(): Int
    abstract fun initBindFun(): BindAction<D>
    abstract fun initType(): Type
    private var fresh = true
    override fun initView() {
        if (initHeader().isNotEmpty()) {
            for (layRes in initHeader()) {
                addTopView(layRes,ll_top)
            }
        }
        if (initScrollableView().isNotEmpty()) {
            for (layRes in initScrollableView()) {
                addTopView(layRes,llScrollableView)
            }
        }
        if (initAlwaysView().isNotEmpty()) {
            for (layRes in initAlwaysView()) {
                addTopView(layRes,appBarLayout)
            }
        }
        action = initBindFun()
        refreshLayout.setEnableRefresh(initType() == Type.BOTH || initType() == Type.TOP)
        if ((initType() == Type.BOTH || initType() == Type.TOP)&&customAutoRefresh()) {
            refreshLayout.autoRefresh()
        }
        refreshLayout.setEnableLoadMore(initType() == Type.BOTH || initType() == Type.BOTTOM)
        if(customNoMoreData()&&(initType() == Type.BOTH || initType() == Type.BOTTOM)){
            refreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        }
    }

    override fun initialized() {
        recyclerView.adapter = mAdapter.apply {
            if(customEmptyView()!=0){
                addEmptyView(inflateView(customEmptyView()))
            }
        }
        if (initType() == Type.BOTH || initType() == Type.TOP) {
            refreshLayout.onRefreshListener {
                fresh = true
                nowPage = 1
                getData()
            }
        }
        if (initType() == Type.BOTH || initType() == Type.BOTTOM) {
            refreshLayout.onLoadMoreListener {
                fresh = false
                nowPage++
                getData()
            }
        }
    }
    open fun inflateView(res: Int) = layoutInflater.inflate(res, recyclerView, false)

    private fun addTopView(headerRes: Int,parentView:ViewGroup) {
        if (headerRes != 0) {
            layoutInflater.inflate(headerRes, parentView, true)
        }
    }

    open fun buildData(list: List<D>?) {
        if (fresh) {
            nowPage = 1
            if (mList.isNotEmpty()) {
                mList.clear()
            }
            if (list.isNullOrEmpty()) {
                then {
                    if(customNoMoreData()){
                        refreshLayout.finishRefresh()
                        refreshLayout.setEnableLoadMore(false)
                        mAdapter.addFooterView(inflateView(R.layout.item_bottom))
                    }else{
                        mAdapter.notifyDataSetChanged()
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    }
                }
            } else {
                mList.addAll(list)
                then {
                    if(customNoMoreData()){
                        refreshLayout.finishRefresh()
                        refreshLayout.setEnableLoadMore(true)
                        mAdapter.removeFooterView()
                    }else{
                        mAdapter.notifyDataSetChanged()
                        refreshLayout.finishRefresh()
                        refreshLayout.resetNoMoreData()
                    }
                }
            }
        } else {
            if (list.isNullOrEmpty()) {
                nowPage--
                then {
                    if(customNoMoreData()){
                        refreshLayout.finishLoadMore()
                        mAdapter.addFooterView(inflateView(R.layout.item_bottom))
                        refreshLayout.setEnableLoadMore(false)
                    }else{
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    }
                }
            } else {
                mList.addAll(list)
                then {
                    if(customNoMoreData()){
                        refreshLayout.finishLoadMore()
                        mAdapter.removeFooterView()
                        refreshLayout.setEnableLoadMore(true)
                    }else{
                        mAdapter.notifyDataSetChanged()
                        refreshLayout.finishLoadMore()
                        refreshLayout.resetNoMoreData()
                    }
                }
            }
        }
    }

    open fun customNoMoreData()=false
    open fun customAutoRefresh()=true
    open fun customEmptyView()=0

    open fun getData() {
        refreshLayout.finishRefresh()
    }

    private class ListAdp<D>(res: Int, list: ArrayList<D>, val action: BindAction<D>) : LBaseAdapter<D>(res, list) {
        override fun bindData(holder: BaseViewHolder, model: D, pos: Int) {
            action.bind(holder, model, pos)
        }
    }
}
