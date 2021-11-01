package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 *
 * User details DB
 */

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDao {
    @Query("SELECT EXISTS(SELECT 1 FROM user_details WHERE id = :id LIMIT 1)")
    fun isUserDetailsAvailable(id: Long): Boolean

    @Query("SELECT * FROM user_details WHERE id = :id")
    fun getUserDetails(id: Long): UserDetails

    @Query("SELECT * FROM user_details WHERE login = :login")
    fun getUserDetailsByLogin(login: String): LiveData<UserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(userDetails: UserDetails)

    @Delete
    suspend fun deleteUserDetails(userDetails: UserDetails)
}
