package infixsoft.imrankst1221.android.starter.utilities

import android.widget.ScrollView

/**
 * @author imran.choudhury
 * 31/10/21
 */
object Extensions {
    fun ScrollView.scrollToBottomWithoutFocusChange() {
        val lastChild = getChildAt(childCount - 1)
        val bottom = lastChild.bottom + paddingBottom
        val delta = bottom - (scrollY + height)
        smoothScrollBy(0, delta)
    }
}