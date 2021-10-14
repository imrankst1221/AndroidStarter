package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.data.model.UserDetails
import infixsoft.imrankst1221.android.starter.data.model.UserDetailsDao
import infixsoft.imrankst1221.android.starter.utilities.getLiveDataValue
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user1
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user2
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user3
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user1Details
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user2Details
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user3Details
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
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
class UserDetailsDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var userDetailsDao: UserDetailsDao

    @Before fun createDb() = runBlocking{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database =  Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDetailsDao = database.userDetailsDao()

        insertUserDetails()
    }

    @After fun closeDb() {
        database.close()
    }

    private fun insertUserDetails() = runBlocking{
        database.userDaoDao().insertAll(listOf(user1, user2, user3))

        userDetailsDao.insertUserDetails(user1Details)
        userDetailsDao.insertUserDetails(user2Details)
        userDetailsDao.insertUserDetails(user3Details)
    }

    @Throws(InterruptedException::class)
    fun testUserDetailsByLogin() = runBlocking{
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(user1.login)), equalTo(user1Details))
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(user2.login)), equalTo(user2Details))
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(user3.login)), equalTo(user3Details))
    }

    @Test fun testIsUserDetailsAvailable() = runBlocking {
        assertThat(userDetailsDao.isUserDetailsAvailable(1), equalTo(true))
        assertThat(userDetailsDao.isUserDetailsAvailable(2), equalTo(true))
        assertThat(userDetailsDao.isUserDetailsAvailable(3), equalTo(true))

        assertThat(userDetailsDao.isUserDetailsAvailable(0), equalTo(false))
        assertThat(userDetailsDao.isUserDetailsAvailable(5), equalTo(false))
        assertThat(userDetailsDao.isUserDetailsAvailable(6), equalTo(false))
    }

    @Test fun testGetUserDetails() = runBlocking {
        assertThat(userDetailsDao.getUserDetails(1), equalTo(user1Details))
        assertThat(userDetailsDao.getUserDetails(2), equalTo(user2Details))
        assertThat(userDetailsDao.getUserDetails(3), equalTo(user3Details))
    }

    @Test fun testUpdateAllUserDetails() = runBlocking{
        val userDetailsA1 = UserDetails(1, "user1","User A", "",1, 1, "Google", "")
        val userDetailsB1 = UserDetails(2, "user2","User B", "",1, 1, "Google", "")
        val userDetailsC1 = UserDetails(3, "user3","User C", "",1, 1, "Google", "")

        // insert new values
        userDetailsDao.insertUserDetails(userDetailsA1)
        userDetailsDao.insertUserDetails(userDetailsB1)
        userDetailsDao.insertUserDetails(userDetailsC1)

        assertThat(userDetailsDao.getUserDetails(1), equalTo(userDetailsA1))
        assertThat(userDetailsDao.getUserDetails(2), equalTo(userDetailsB1))
        assertThat(userDetailsDao.getUserDetails(3), equalTo(userDetailsC1))

        // back to previous value
        insertUserDetails()
        testIsUserDetailsAvailable()
        testGetUserDetails()
    }
}