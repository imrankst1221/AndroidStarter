package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.data.model.UserDetails
import infixsoft.imrankst1221.android.starter.data.model.UserDetailsDao
import infixsoft.imrankst1221.android.starter.utilities.getLiveDataValue
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER2
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER3
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1_DETAILS
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER2_DETAILS
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER3_DETAILS
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
        database.userDaoDao().insertAll(listOf(DUMMY_USER1, DUMMY_USER2, DUMMY_USER3))

        userDetailsDao.insertUserDetails(DUMMY_USER1_DETAILS)
        userDetailsDao.insertUserDetails(DUMMY_USER2_DETAILS)
        userDetailsDao.insertUserDetails(DUMMY_USER3_DETAILS)
    }

    @Throws(InterruptedException::class)
    fun testUserDetailsByLogin() = runBlocking{
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(DUMMY_USER1.login)), equalTo(DUMMY_USER1_DETAILS))
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(DUMMY_USER2.login)), equalTo(DUMMY_USER2_DETAILS))
        assertThat(getLiveDataValue(userDetailsDao.getUserDetailsByLogin(DUMMY_USER3.login)), equalTo(DUMMY_USER3_DETAILS))
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
        assertThat(userDetailsDao.getUserDetails(1), equalTo(DUMMY_USER1_DETAILS))
        assertThat(userDetailsDao.getUserDetails(2), equalTo(DUMMY_USER2_DETAILS))
        assertThat(userDetailsDao.getUserDetails(3), equalTo(DUMMY_USER3_DETAILS))
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