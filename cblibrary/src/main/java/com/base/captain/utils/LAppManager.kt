package com.base.captain.utils

import android.app.ActivityManager
import com.base.captain.App
import com.base.captain.ui.LBaseActivity
import java.util.*
import kotlin.system.exitProcess


object LAppManager {
    private val activityStack = Stack<LBaseActivity>()
    fun getNowActivity() = activityStack[activityStack.size - 1]
    fun addActivity(aty: LBaseActivity) {
        activityStack.add(aty)
    }

    fun getSize() = activityStack.size

    fun finishActivity(activity: LBaseActivity) {
        activityStack.remove(activity)
        activity.finish()
    }
    fun finishTop(num:Int){
        try{
            val size = activityStack.size
            for (i in size-1 downTo size-num){
                activityStack[i].finish()
            }
        }catch (e:Exception){}
    }
    fun finishAllExportMain() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i] && activityStack[i].javaClass.name != "com.dieyu.tklm.activity.MainActivity") {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    fun AppExit() {
        try {
            finishAllActivity()
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } catch (e: Exception) {
        }
    }

    fun isRunningForeground(): Boolean {
        val runningProcesses = App.ATY_MANAGER.runningAppProcesses as List<ActivityManager.RunningAppProcessInfo>
        loop@ for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (processInfo.processName == App.PCK_NAME) {
                    return true
                }
            }
        }
        return false
    }
}