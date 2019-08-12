package com.base.captain.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.base.captain.utils.LAppManager
import com.base.captain.utils.LDMUtils
import com.base.captain.utils.LStatusBarHelper
import com.base.captain.widget.LLoadingDialog

abstract class LBaseActivity : AppCompatActivity() {
    lateinit var loading: MutableLiveData<Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LAppManager.addActivity(this)
        LDMUtils.setCustomDensity(this, application, 375)
        if(isTranslucent()){
            LStatusBarHelper.translucent(this)
            setStateBar(isWhite())
        }
        if (setLayout() != 0) {
            setContentView(setLayout())
            initView()
        }
        initialized()
        if(::loading.isInitialized){
            loading.observe(this, Observer { if (it) showLoading() else hideLoading() })
        }
    }

    private lateinit var progressDialog: LLoadingDialog
    open fun showLoading(msg: String = "正在请求...") {
        if (!::progressDialog.isInitialized) {
            progressDialog = LLoadingDialog(this, msg,false)
        }
        progressDialog.show()
    }

    open fun hideLoading() {
        if (::progressDialog.isInitialized) {
            progressDialog.dismiss()
        }
    }

    fun setStateBar(white: Boolean) {
        if (white) {
            LStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            LStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    abstract fun setLayout(): Int
    open fun isWhite(): Boolean = false
    open fun isTranslucent(): Boolean = true
    open fun initView() {}
    open fun initialized() {}
    override fun onDestroy() {
        super.onDestroy()
        LAppManager.finishActivity(this)
    }
}