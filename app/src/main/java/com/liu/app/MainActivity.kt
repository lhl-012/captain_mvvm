package com.liu.app

import com.base.captain.adapter.BaseViewHolder
import com.base.captain.ui.LBaseListActivity
import com.base.captain.utils.LToast
import com.base.captain.utils.load

class MainActivity : LBaseListActivity<String>() {
    override fun initType() = Type.BOTH//设置刷新的类型   NONE, TOP, BOTTOM, BOTH

    override fun initHeader()= listOf(R.layout.base_header)  //顶部布局，可放入多个布局id，addView方式实现
    override fun itemLayout() = R.layout.activity_main//列表item布局
    override fun initBindFun() = object : BindAction<String> {
        override fun bind(holder: BaseViewHolder, model: String, pos: Int) {
            holder.setText(R.id.tv_name, "item $model").setOnClick(R.id.tv_name) {
                LToast.show("$pos click")
            }
        }
    }

    override fun customNoMoreData()=true//是否用自定义没有更多数据

    override fun customAutoRefresh()=false//是否自动执行一次刷新
//    override fun customEmptyView()=R.layout.item_empty//是否自定义emptyview   也可以写布局item_empty.xml

    override fun initView() {
        super.initView()
        addHeader(inflateView(R.layout.header_ttt))//添加头部view  随列表滚动
    }
    override fun getData() {
        load {
            //异步模拟网络
//            val resp=url.readJson<T>(type)
            //更新数据   buildData(List<T>)
            buildData(ArrayList<String>().apply {
                if(nowPage==1){
                    add("i.toString()")
                    add("i.toString()")
                }
            })
        }.start()
    }
}
