package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.MainCoroutineRule
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER2
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER3
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import infixsoft.imrankst1221.android.starter.utilities.getLiveDataValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws


/**
 * @author imran.choudhury
 * 18/9/21
 */

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private val coroutineRule = MainCoroutineRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before fun createDb() = runBlocking{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database =  Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = database.userDaoDao()

        insertUsers()
    }

    @After fun closeDb() {
        database.close()
    }

    private fun insertUsers() = runBlocking{
        userDao.insertAll(listOf(DUMMY_USER1, DUMMY_USER2, DUMMY_USER3))
    }

    @Throws(InterruptedException::class)
    fun testGetAllUsers() = runBlocking{
        val userItems = userDao.getAllUsers()
        assertThat(userItems.size, Matchers.equalTo(3))

        assertThat(userItems.size, Matchers.equalTo(3))
        assertThat(userItems[0], equalTo(DUMMY_USER1))
        assertThat(userItems[1], equalTo(DUMMY_USER2))
        assertThat(userItems[2], equalTo(DUMMY_USER3))
    }


    @Throws(InterruptedException::class)
    fun testUsers() = runBlocking{
        val userItems = getLiveDataValue(userDao.getUsersWithNote())

        assertThat(userItems.size, Matchers.equalTo(3))
        assertThat(userItems[0], equalTo(DUMMY_USER1))
        assertThat(userItems[1], equalTo(DUMMY_USER2))
        assertThat(userItems[2], equalTo(DUMMY_USER3))
    }

    @Test fun testUpdateAllUser() = runBlocking{
        val userA = User(1, "Test 1", "", "")
        val userB = User(2, "Test 2", "", "")
        val userC = User(3, "Test 3", "", "")

        // insert new values
        userDao.insertAll(listOf(userA, userC, userB))
        val userItems = userDao.getAllUsers()
        assertThat(userItems.size, Matchers.equalTo(3))
        assertThat(userItems[0], equalTo(userA))
        assertThat(userItems[1], equalTo(userB))
        assertThat(userItems[2], equalTo(userC))

        // back to previous value
        insertUsers()
        testGetAllUsers()
    }

}