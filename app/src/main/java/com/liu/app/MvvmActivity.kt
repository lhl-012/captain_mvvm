package com.liu.app

import androidx.lifecycle.Observer
import com.base.captain.getViewModel
import com.base.captain.ui.LBaseActivity
import kotlinx.android.synthetic.main.activity_mvvm.*


class MvvmActivity : LBaseActivity() {
    override fun setLayout() = R.layout.activity_mvvm
    override fun isWhite()=true
    override fun initialized() {
        val vm = getViewModel<MyVM>()
        loading=vm.loading
        vm.res.observe(this, Observer { textView.text = it.toString() })
        vm.getData()
    }
}
data class Result(val content: Content, val error: Int)
data class Content(
    val downloadurl: String, val lastforceversion_code: String, val log: String, val market: Int,
    val type: String, val versioncode: String, val versionname: String
)
