package com.base.captain.adapter

import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.captain.R

/**
 * Created by Bert on 2018/7/25.
 */
abstract class LBaseAdapter<D>(private val layoutRes: Int, private val data: List<D>) : RecyclerView.Adapter<BaseViewHolder>() {
    private val TYPE_HEAD = 0
    private val TYPE_CONTENT = 1
    private val TYPE_FOOTER = 2
    private val TYPE_EMPTY = 3
    private var HEAD_COUNT = 0
    open var FOOT_COUNT = 0
    private var EMPTY_COUNT = 1
    private val contentSize: Int get() = data.size
    private var mHeadView: View? = null
    private var mFootView: View? = null
    private var mEmptyView: View? = null

    fun setOnItemClickListener(recyclerView: RecyclerView, action: (Int) -> Unit) {
        val mGestureDetector =
            GestureDetectorCompat(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(p0: RecyclerView, p1: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if (childView != null && mGestureDetector.onTouchEvent(e)) {
                    val pos = rv.getChildAdapterPosition(childView)
                    if (isHead(pos) || isFoot(pos) || isEmpty(pos)) {
                        return false
                    }
                    action(pos - HEAD_COUNT)
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(p0: Boolean) {
            }
        })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (getItemViewType(position) == TYPE_CONTENT)
                    1
                else
                    manager.spanCount
            }
        }
    }

    private fun isHead(position: Int): Boolean {
        return HEAD_COUNT != 0 && position == 0
    }

    private fun isFoot(position: Int): Boolean {
        return if (contentSize == 0) {
            FOOT_COUNT != 0 && position == contentSize + HEAD_COUNT + EMPTY_COUNT
        } else {
            FOOT_COUNT != 0 && position == contentSize + HEAD_COUNT
        }
    }

    private fun isEmpty(position: Int): Boolean {
        return if (contentSize == 0) {
            EMPTY_COUNT != 0 && position == 0 + HEAD_COUNT
        } else {
            false
        }
    }


    override fun getItemViewType(position: Int): Int {
        val contentSize = contentSize
        return if (contentSize == 0) {
            //没数据
            if (HEAD_COUNT != 0 && position == 0) {
                TYPE_HEAD
            } else if (EMPTY_COUNT != 0 && position == 0 + HEAD_COUNT) {
                TYPE_EMPTY
            } else {
                TYPE_FOOTER
            }
        } else {
            //有数据
            if (HEAD_COUNT != 0 && position == 0) {
                TYPE_HEAD
            } else if (FOOT_COUNT != 0 && position == HEAD_COUNT + contentSize) { // 尾部
                TYPE_FOOTER
            } else {
                TYPE_CONTENT
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_HEAD -> BaseViewHolder(mHeadView!!)
            TYPE_CONTENT -> BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
            TYPE_EMPTY -> BaseViewHolder(mEmptyView ?: LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false))
            else -> BaseViewHolder(mFootView!!)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if(isHead(position)||isFoot(position)||isEmpty(position)){
        }else{
            bindData(holder, data[position - HEAD_COUNT], position - HEAD_COUNT)
        }
    }

    abstract fun bindData(holder: BaseViewHolder, model: D, pos: Int)

    override fun getItemCount(): Int {
        return if (data.isEmpty()) {
            HEAD_COUNT + FOOT_COUNT + EMPTY_COUNT
        } else {
            data.size + HEAD_COUNT + FOOT_COUNT
        }
    }

    fun addHeaderView(view: View) {
        HEAD_COUNT = 1
        mHeadView = view
        notifyDataSetChanged()
    }

    fun removeHeaderView() {
        HEAD_COUNT = 0
        mHeadView = null
        notifyDataSetChanged()
    }

    fun addEmptyView(view: View) {
        EMPTY_COUNT = 1
        mEmptyView = view
        notifyDataSetChanged()
    }

    fun removeEmptyView() {
        EMPTY_COUNT = 0
        mEmptyView = null
        notifyDataSetChanged()
    }

    fun haveFooterView()=FOOT_COUNT==1

    fun addFooterView(view: View) {
        FOOT_COUNT = 1
        mFootView = view
        notifyDataSetChanged()
    }

    fun removeFooterView() {
        FOOT_COUNT = 0
        mFootView = null
        notifyDataSetChanged()
    }
}
