package infixsoft.imrankst1221.android.starter.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import infixsoft.imrankst1221.android.starter.MainCoroutineRule
import infixsoft.imrankst1221.android.starter.data.model.UserNote
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import infixsoft.imrankst1221.android.starter.runBlockingTest
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.*
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.jvm.Throws

/**
 * @author imran.choudhury
 * 4/10/21
 */

@HiltAndroidTest
@ExperimentalCoroutinesApi
class UsersViewModelTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var usersViewModel: UsersViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val coroutineRule = MainCoroutineRule()
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp(){
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        usersViewModel = UsersViewModel(userRepository)
    }

    @After fun closeDb() {
        appDatabase.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = runBlocking(){
        usersViewModel.loadMoreUsers()
    }
}