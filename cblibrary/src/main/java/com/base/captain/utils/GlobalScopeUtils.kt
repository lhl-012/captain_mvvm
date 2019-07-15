package com.base.captain.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

fun LifecycleOwner.delayLife(time: Long = 2000, action: () -> Unit) {
    val deferred = GlobalScope.async {
        delay(time)
        action()
    }
    lifecycle.addObserver(CoroutineLifecycleListener(deferred))
    deferred.start()
}

fun <T> LifecycleOwner.load(loadFunction: () -> T): Deferred<T> {
    val deferred = GlobalScope.async(context = Dispatchers.IO, start = CoroutineStart.LAZY) { loadFunction() }
    lifecycle.addObserver(CoroutineLifecycleListener(deferred))
    return deferred
}

infix fun <T> Deferred<T>.then(uiFunction: (T) -> Unit) {
    GlobalScope.launch(context = Dispatchers.Main) { uiFunction(this@then.await()) }
}
infix fun LifecycleOwner.then(uiFunction: () -> Unit) {
    GlobalScope.launch(context = Dispatchers.Main) { uiFunction() }
}

class CoroutineLifecycleListener(private val deferred: Deferred<*>) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelCoroutine() {
        if (!deferred.isCancelled) {
            deferred.cancel()
        }
    }
}