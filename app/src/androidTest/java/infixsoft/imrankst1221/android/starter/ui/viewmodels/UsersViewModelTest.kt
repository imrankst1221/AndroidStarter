package infixsoft.imrankst1221.android.starter.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import infixsoft.imrankst1221.android.starter.MainCoroutineRule
import infixsoft.imrankst1221.android.starter.data.AppDatabase
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import kotlinx.coroutines.*
import org.junit.*
import org.junit.rules.RuleChain
import javax.inject.Inject
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
        usersViewModel.fetchUserDetails(DUMMY_USER1.login)
    }
}