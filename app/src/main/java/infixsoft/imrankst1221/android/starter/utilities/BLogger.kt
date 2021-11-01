package infixsoft.imrankst1221.android.starter.utilities

import android.util.Log
import infixsoft.imrankst1221.android.starter.BuildConfig

/**
 * @author imran.choudhury
 * 1/11/21
 *
 * BLogger class is printing Logcat on debug mode
 */

object BLogger {
    private val SHOULD_LOG: Boolean = BuildConfig.DEBUG

    @JvmStatic
    fun i(tag: String, string: String) {
        if (SHOULD_LOG) Log.i(tag, string)
    }

    @JvmStatic
    fun e(tag: String, string: String) {
        if (SHOULD_LOG) Log.e(tag, string)
    }

    @JvmStatic
    fun d(tag: String, string: String) {
        if (SHOULD_LOG) Log.d(tag, string)
    }

    @JvmStatic
    fun v(tag: String, string: String) {
        if (SHOULD_LOG) Log.v(tag, string)
    }

    @JvmStatic
    fun w(tag: String, string: String) {
        if (SHOULD_LOG) Log.w(tag, string)
    }
}