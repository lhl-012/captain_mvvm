package com.liu

import com.base.captain.App

class TestApp : App() {
    override fun onCreate() {
        super.onCreate()
        IP = "http://www.baidu.com/"
        debug=true
    }
}