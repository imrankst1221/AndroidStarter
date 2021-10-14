package infixsoft.imrankst1221.android.starter.ui.view

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.ui.views.MainActivity
import infixsoft.imrankst1221.android.starter.ui.views.UserListFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author imran.choudhury
 * 14/10/21
 */

@RunWith(AndroidJUnit4::class)
class UserListFragmentTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp(){
        activityTestRule.activity.apply {
            runOnUiThread {
                //val bundle = Bundle().apply { putString("", "") }
            }
        }
    }

    @Test
    fun testEventFragment() {
        val bundle = Bundle().apply { putString("", "") }
        val scenario = launchFragmentInContainer<UserListFragment>(
            fragmentArgs = bundle,
        )
    }
}