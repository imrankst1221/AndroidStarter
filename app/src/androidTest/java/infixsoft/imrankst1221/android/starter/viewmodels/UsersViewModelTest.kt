package infixsoft.imrankst1221.android.starter.data

import android.content.Context
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import infixsoft.imrankst1221.android.starter.MainCoroutineRule
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import infixsoft.imrankst1221.android.starter.runBlockingTest
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.utilities.getValue
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import kotlin.jvm.Throws

/**
 * @author imran.choudhury
 * 4/10/21
 */

@HiltAndroidTest
class UsersViewModelTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var usersViewModel: UsersViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val coroutineRule = MainCoroutineRule()

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
    fun testDefaultValues() = coroutineRule.runBlockingTest {

    }

}