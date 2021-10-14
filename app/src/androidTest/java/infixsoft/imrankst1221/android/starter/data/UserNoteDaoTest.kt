package infixsoft.imrankst1221.android.starter.data

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER1_NOTE
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER2
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER2_NOTE
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER3
import infixsoft.imrankst1221.android.starter.data.DummyDataSet.DUMMY_USER3_NOTE
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
        database.userDaoDao().insertAll(listOf(DUMMY_USER1, DUMMY_USER2, DUMMY_USER3))
        userNoteDao.insertNote(DUMMY_USER1_NOTE)
        userNoteDao.insertNote(DUMMY_USER2_NOTE)
        userNoteDao.insertNote(DUMMY_USER3_NOTE)
    }

    @Test fun testIsNoteAvailable() = runBlocking {
        assertThat(userNoteDao.getNote(DUMMY_USER1.userId).note.isNullOrEmpty(), equalTo(false))
        assertThat(userNoteDao.getNote(DUMMY_USER2.userId).note.isNullOrEmpty(), equalTo(false))
        assertThat(userNoteDao.getNote(DUMMY_USER3.userId).note.isNullOrEmpty(), equalTo(false))
    }

    @Test fun testUpdateNotes() = runBlocking{
        val note1 = UserNote(DUMMY_USER1.userId, "1 note update")
        val note2 = UserNote(DUMMY_USER2.userId, "2 note update")
        val note3 = UserNote(DUMMY_USER3.userId, "3 note update")

        // insert new values
        userNoteDao.insertNote(note1)
        userNoteDao.insertNote(note2)
        userNoteDao.insertNote(note3)

        assertThat(userNoteDao.getNote(DUMMY_USER1.userId), equalTo(note1))
        assertThat(userNoteDao.getNote(DUMMY_USER2.userId), equalTo(note2))
        assertThat(userNoteDao.getNote(DUMMY_USER3.userId), equalTo(note3))


        // back to previous value
        insertUserNotes()
    }
}