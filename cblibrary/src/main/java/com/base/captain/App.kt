package com.base.captain

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.base.captain.utils.LBroadcastUtils
import com.base.captain.utils.LSPUtils
import com.base.captain.utils.LToast
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import java.io.File

open class App : Application() {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            layout.setEnableOverScrollDrag(false)
            layout.setEnableOverScrollBounce(false)
            layout.setEnableLoadMoreWhenContentNotFull(false)
            layout.setEnableScrollContentWhenRefreshed(true)
            layout.setDisableContentWhenRefresh(true)
            layout.setDisableContentWhenLoading(true)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.bg, R.color.style_color_primary)
            ClassicsHeader(context).setEnableLastTime(false).setSpinnerStyle(SpinnerStyle.FixedBehind)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.bg, R.color.style_color_primary)
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.FixedBehind)
        }
    }

    companion object {
        var IP = ""
        lateinit var ATY_MANAGER: ActivityManager
        var PCK_NAME = ""
        var TASK_ID = 0
        var appCtx: Application? = null
        var debug = false
    }

    override fun onCreate() {
        super.onCreate()
        appCtx = this
        LSPUtils.init(applicationContext)
        LToast.init(applicationContext)
        LBroadcastUtils.init(applicationContext)
        ATY_MANAGER = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        PCK_NAME = packageName
        if (debug) {
            Thread.setDefaultUncaughtExceptionHandler { _, e ->
                val file = File(applicationContext.getExternalFilesDir(""), "app.log")
                if (file.exists() && file.isDirectory) {
                } else {
                    file.createNewFile()
                }
                file.writeText(e.localizedMessage + "\r\n")
            }
        }
    }
}