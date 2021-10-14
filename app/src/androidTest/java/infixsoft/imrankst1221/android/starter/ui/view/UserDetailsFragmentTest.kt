package infixsoft.imrankst1221.android.starter.ui.view

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import infixsoft.imrankst1221.android.starter.MainCoroutineRule
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.data.AppDatabase
import infixsoft.imrankst1221.android.starter.data.DummyDataSet
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1_DETAILS
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.ui.views.MainActivity
import infixsoft.imrankst1221.android.starter.ui.views.UserDetailsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.jvm.Throws

/**
 * @author imran.choudhury
 * 14/10/21
 */

@HiltAndroidTest
@ExperimentalCoroutinesApi
class UserDetailsFragmentTest {

    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var usersViewModel: UsersViewModel

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)
        .around(activityTestRule)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp(){
        hiltRule.inject()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        usersViewModel = UsersViewModel(userRepository)

        val bundle = Bundle().apply {
            putSerializable("user", DUMMY_USER1)
        }
        activityTestRule.activity.apply {
            runOnUiThread {
                findNavController(R.id.userNavHostFragment).navigate(
                    R.id.action_userListFragment_to_userDetailsFragment,
                    bundle
                )
            }
        }

        insertData()
    }

    private fun insertData() = runBlocking{
        appDatabase.userDaoDao().insertAll(listOf(DUMMY_USER1,
            DummyDataSet.DUMMY_USER2,
            DummyDataSet.DUMMY_USER3
        ))

        appDatabase.userDetailsDao().insertUserDetails(DUMMY_USER1_DETAILS)
        appDatabase.userDetailsDao().insertUserDetails(DummyDataSet.DUMMY_USER2_DETAILS)
        appDatabase.userDetailsDao().insertUserDetails(DummyDataSet.DUMMY_USER3_DETAILS)

        appDatabase.userNoteDao().insertNote(DummyDataSet.DUMMY_USER1_NOTE)
        appDatabase.userNoteDao().insertNote(DummyDataSet.DUMMY_USER2_NOTE)
        appDatabase.userNoteDao().insertNote(DummyDataSet.DUMMY_USER3_NOTE)
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testEventFragment(){
        val scenario = launchFragment<UserDetailsFragment>()
        scenario.onFragment { fragment ->
            val userDetails = DUMMY_USER1_DETAILS
            onView(withId(R.id.tvName)).check(matches(withText(userDetails.name)))
            onView(withId(R.id.tvCompany)).check(matches(withText(userDetails.company)))
            onView(withId(R.id.tvBlog)).check(matches(withText(userDetails.blog)))
        }
    }
}