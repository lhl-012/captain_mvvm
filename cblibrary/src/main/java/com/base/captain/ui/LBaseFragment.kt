package com.base.captain.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

abstract class LBaseFragment : Fragment() {
    lateinit var loading: MutableLiveData<Boolean>
    open var mContainer: ViewGroup?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer=container
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initialized()
    }

    abstract fun setLayout(): Int
    open fun initView() {}
    open fun initialized() {}
}