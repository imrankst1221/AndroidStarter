package infixsoft.imrankst1221.android.starter.data.repository

import infixsoft.imrankst1221.android.starter.data.model.UserDetailsDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author imran.choudhury
 * 30/9/21
 */

@Singleton
class UserDetailsRepository @Inject constructor(private val userDetailsDao: UserDetailsDao){
    fun getUserDetails(userId: Long) = userDetailsDao.getUserDetails(userId)
    fun isUserNote(userId: Long) = userDetailsDao.isUserDetailsAvailable(userId)
}