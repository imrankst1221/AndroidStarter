package infixsoft.imrankst1221.android.starter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import infixsoft.imrankst1221.android.starter.data.model.*
import infixsoft.imrankst1221.android.starter.utilities.Constants

/**
 * @author imran.choudhury
 * 18/9/21
 *
 * AppDatabase is Room database helper
 *
 */


@Database(entities = [User::class, UserDetails::class, UserNote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDaoDao(): UserDao
    abstract fun userDetailsDao(): UserDetailsDao
    abstract fun userNoteDao(): UserNoteDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }
                )
                .build()
        }
    }
}
