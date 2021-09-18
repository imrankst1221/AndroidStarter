package infixsoft.imrankst1221.android.starter.utilities

import android.util.Log
import infixsoft.imrankst1221.android.starter.BuildConfig

/**
 * This class is used to replace [Log] as a safe logger since it check
 * if the current BuildConfig is DEBUG or not, if yes, then it logs the message
 * otherwise not.
 * Using this logger will make sure no log is printed in release build
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