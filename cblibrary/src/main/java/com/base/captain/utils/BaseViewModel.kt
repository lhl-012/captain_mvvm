package com.base.captain.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.captain.HttpType
import com.base.captain.readJson
import com.base.captain.readJsonVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    inline fun <reified T> loadJson(url: String, crossinline successFun: (T) -> Unit, crossinline errorFun: (String) -> Unit,type:HttpType=HttpType.GET,vararg params: Pair<String, Any?>) {
        loading.postValue(true)
        viewModelScope.launch (context = Dispatchers.IO){
            val resp=url.readJsonVM<T>(type,params)
            if(null==resp){
                errorFun("unknown")
            }else{
                successFun(resp)
            }
            loading.postValue(false)
        }
    }
}