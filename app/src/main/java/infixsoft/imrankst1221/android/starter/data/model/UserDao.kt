package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 */

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY login")
    fun getUserItems(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserItem(userId: Long): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)
}
