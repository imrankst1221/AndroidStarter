package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user1
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user1Note
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user2
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user2Note
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user3
import infixsoft.imrankst1221.android.starter.data.TestDataSet.user3Note
import infixsoft.imrankst1221.android.starter.data.model.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author imran.choudhury
 * 18/9/21
 */

@RunWith(AndroidJUnit4::class)
class UserNoteDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var userNoteDao: UserNoteDao

    @Before fun createDb() = runBlocking{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database =  Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userNoteDao = database.userNoteDao()

        insertUserNotes()
    }

    @After fun closeDb() {
        database.close()
    }

    private fun insertUserNotes() = runBlocking{
        database.userDaoDao().insertAll(listOf(user1, user2, user3))
        userNoteDao.insertNote(user1Note)
        userNoteDao.insertNote(user2Note)
        userNoteDao.insertNote(user3Note)
    }

    @Test fun testIsNoteAvailable() = runBlocking {
        assertThat(userNoteDao.getNote(user1.userId).note.isNullOrEmpty(), equalTo(false))
        assertThat(userNoteDao.getNote(user2.userId).note.isNullOrEmpty(), equalTo(false))
        assertThat(userNoteDao.getNote(user3.userId).note.isNullOrEmpty(), equalTo(false))
    }

    @Test fun testUpdateNotes() = runBlocking{
        val note1 = UserNote(user1.userId, "1 note update")
        val note2 = UserNote(user2.userId, "2 note update")
        val note3 = UserNote(user3.userId, "3 note update")

        // insert new values
        userNoteDao.insertNote(note1)
        userNoteDao.insertNote(note2)
        userNoteDao.insertNote(note3)

        assertThat(userNoteDao.getNote(user1.userId), equalTo(note1))
        assertThat(userNoteDao.getNote(user2.userId), equalTo(note2))
        assertThat(userNoteDao.getNote(user3.userId), equalTo(note3))


        // back to previous value
        insertUserNotes()
    }
}