package infixsoft.imrankst1221.android.starter.utilities

import android.widget.ScrollView
import androidx.lifecycle.MutableLiveData

/**
 * @author imran.choudhury
 * 31/10/21
 *
 * Android Extensions helper
 */
object Extensions {
    // scrollview logic for keyboard
    fun ScrollView.scrollToBottomWithoutFocusChange() {
        val lastChild = getChildAt(childCount - 1)
        val bottom = lastChild.bottom + paddingBottom
        val delta = bottom - (scrollY + height)
        smoothScrollBy(0, delta)
    }

    // notify live data change
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}