package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 */

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDao {
    @Query("SELECT EXISTS(SELECT 1 FROM user_details WHERE id = :id LIMIT 1)")
    fun isUserDetailsAvailable(id: Long): Boolean

    @Query("SELECT * FROM user_details WHERE id = :id")
    fun getUserDetails(id: Long): UserDetails

    @Query("SELECT note FROM user_details WHERE id = :id LIMIT 1")
    fun getDetailNotes(id: Long): String

    @Query("UPDATE user_details SET note=:note WHERE id = :id")
    suspend fun updateNotes(id: Long, note: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(userDetails: UserDetails)

    @Delete
    suspend fun deleteUserDetails(userDetails: UserDetails)
}
