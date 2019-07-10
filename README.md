####Captain_MVVM    androidx版本

[![](https://jitpack.io/v/lhl-012/captain_mvvm.svg)](https://jitpack.io/#lhl-012/captain_mvvm)

###项目依赖

```
    api 'androidx.appcompat:appcompat:1.0.2'
    api 'androidx.recyclerview:recyclerview:1.0.0'
    api 'com.github.bumptech.glide:glide:4.9.0'
    api 'com.google.code.gson:gson:2.8.5'
    api 'com.squareup.okhttp3:okhttp:4.0.0'
    api "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    api 'org.jetbrains.anko:anko-commons:0.10.8'
    api 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0-rc01'
```

```
解压A_KTVM.rar到android-studio\plugins\android\lib\templates根目录下，重启android-studio，可使用模板KTVM

包括Activity+ViewModel  Adapter    Fragment+ViewModel   LBaseListActivity   LBaseListFragment
```
###1.LBaseListActivity(LBaseListFragment基本完全一致)

```

class MainActivity : LBaseListActivity<String>() {

    override fun initType() = Type.BOTH //设置刷新的类型   NONE, TOP, BOTTOM, BOTH
    
    override fun initHeader()= listOf(R.layout.base_header)//此处设置头  比如titlebar  或者不随滚动的view  add到view中，可以通过ktx插件直接id使用
    
    override fun itemLayout() = R.layout.activity_main//此处设置列表item布局
    
    override fun initBindFun() = object : BindAction<String> {
            override fun bind(holder: BaseViewHolder, model: String, pos: Int) {
                holder.setText(R.id.tv_name, "item $model").setOnClick(R.id.tv_name) {
                    LToast.show("$pos click")
                }
            }
        }

    override fun customNoMoreData()=true//设置是否使用自定义结尾   如果使用设置为true，并自建item_bottom.xml自动启用

    override fun customAutoRefresh()=false//是否启用自动刷新
    
    override fun initView() {//也可以重写initialized  在里面实现
            super.initView()//一定要有此处
            tv_title?.setOnClickListener {//头部中的id可以直接使用
                buildData(null)
            }
            addHeader(inflateView(R.layout.header_ttt))//添加列表的头布局，会随着滑动
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

```
###2.LBaseActivity(LBaseFragment)

```
class MvvmActivity : LBaseActivity() {
    override fun setLayout() = R.layout.activity_mvvm
    override fun isWhite()=true  //设置状态栏字体颜色
    override fun initialized() {
        val vm = getViewModel<MyVM>()
        loading=vm.loading//设置loading   添加后网络请求会自动弹dialog
        vm.res.observe(this, Observer { textView.text = it.toString() })  //viewmodel中需要观察的数据
        vm.getData()//自定义方法   执行加载
    }
}
```
###3.网络请求
String扩展(需要自己异步)，后面的参数为可变参数，key-value形式
```
        load {//协程异步
               url.readJson<T>(HttpType.GET)//无参get
               url.readJson<T>(HttpType.GET,"a" to "index","page" to 1)//有参get
               url.readJson<T>(HttpType.POST)//无参post
               url.readJson<T>(HttpType.POST,"a" to "index","page" to 1)//有参post
        }.start()
```
Viewmode中使用,直接调用(参数说明)

T:json类型

第一个参数：URL (可以带http的完整地址，也可以是部分，但是需要App.IP=baseurl)

第二个参数：成功回调方法

第三个参数：失败回调方法

第四个参数：请求方法（HttpType.GET/POST）

剩下的可变参数：key to value形式（key必须是String，val可以是int，double,long等，包括file）
```
loadJson<T>("http://xxxxt/index.php?g=Mapp", {
            res.postValue(it)
        }, {

        },HttpType.GET,"m" to "Upload","type" to 1)
```




