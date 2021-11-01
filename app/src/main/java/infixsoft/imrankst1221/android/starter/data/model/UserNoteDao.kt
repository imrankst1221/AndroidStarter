package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 *
 * User note DB
 */

import androidx.room.*

@Dao
interface UserNoteDao {
    @Query("SELECT * FROM userNote WHERE id = :id LIMIT 1")
    fun getNote(id: Long): UserNote

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(userNote: UserNote)
}
