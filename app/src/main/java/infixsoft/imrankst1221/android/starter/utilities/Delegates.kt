package infixsoft.imrankst1221.android.starter.utilities

import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T) : Lazy<Deferred<T>>{
    return lazy{
        GlobalScope.async(start= CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}