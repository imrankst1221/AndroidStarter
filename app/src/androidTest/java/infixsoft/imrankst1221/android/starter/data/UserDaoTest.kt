package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author imran.choudhury
 * 18/9/21
 */

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private val user1 = User(1, "mojombo", "https://avatars.githubusercontent.com/u/1?v=4")
    private val user2 = User(2, "defunkt", "https://avatars.githubusercontent.com/u/2?v=4")
    private val user3 = User(3, "defunkt", "https://avatars.githubusercontent.com/u/4?v=4")


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
        userDao.insertAll(listOf(user1, user2, user3))
    }

    @Test fun testGetAllUsers() = runBlocking {
        val userItems = userDao.getUserItems().first()
        assertThat(userItems.size, Matchers.equalTo(3))

        assertThat(userItems[0], equalTo(user1))
        assertThat(userItems[1], equalTo(user2))
        assertThat(userItems[2], equalTo(user3))
    }

    @Test fun testGetUser() = runBlocking {
        assertThat(userDao.getUserItem(user3.userId), equalTo(user3))
    }

    @Test fun testUpdateAllUser() = runBlocking{
        val userA = User(1, "Test 1", "")
        val userB = User(2, "Test 2", "")
        val userC = User(3, "Test 3", "")

        // insert new values
        userDao.insertAll(listOf(userA, userC, userB))
        val userItems = userDao.getUserItems().first()
        assertThat(userItems.size, Matchers.equalTo(3))
        assertThat(userItems[0], equalTo(userA))
        assertThat(userItems[1], equalTo(userB))
        assertThat(userItems[2], equalTo(userC))

        // back to previous value
        insertUsers()
        testGetAllUsers()
    }

}