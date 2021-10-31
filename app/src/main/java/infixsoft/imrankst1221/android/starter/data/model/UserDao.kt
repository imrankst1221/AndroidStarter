package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 */

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT users.*, userNote.note AS userNote FROM users LEFT JOIN userNote ON users.id = userNote.id")
    fun getUsersWithNote(): List<User>

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: Long): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)
}
