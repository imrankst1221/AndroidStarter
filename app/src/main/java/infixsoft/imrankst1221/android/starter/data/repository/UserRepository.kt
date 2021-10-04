package infixsoft.imrankst1221.android.starter.data.repository

import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infixsoft.imrankst1221.android.starter.data.api.SafeApiRequest
import infixsoft.imrankst1221.android.starter.data.api.UserApiService
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import infixsoft.imrankst1221.android.starter.utilities.Coroutines
import infixsoft.imrankst1221.android.starter.utilities.PreferenceProvider
import infixsoft.imrankst1221.android.starter.utilities.now
import infixsoft.imrankst1221.android.starter.utilities.timediff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author imran.choudhury
 * 30/9/21
 */

const val MINIMUM_INTERVAL = 300

@Singleton
class UserRepository @Inject constructor(
    //private val prefs: PreferenceProvider,
    private val userDao: UserDao,
    private val service: UserApiService): SafeApiRequest(){

    private val users = MutableLiveData<List<User>>()
    init {
        users.observeForever{
            saveUsers(it)
        }
    }

    private fun saveUsers(users: List<User>) {
        Coroutines.io {
            //prefs.saveLastSavedAt(now())
            userDao.insertAll(users)
        }
    }

    suspend fun getUsers(): LiveData<List<User>> {
        return withContext(Dispatchers.IO) {
            fetchUsers()
            userDao.getUsers()
        }
    }

    /*private suspend fun fetchUsers() {
        val lastSavedAt = prefs.getLastSavedAt()
        if (lastSavedAt == null || isFetchNeeded(lastSavedAt)) {
            val response = apiRequest { service.getUsers(0) }
            users.postValue(response)
        }
    }*/
    private suspend fun fetchUsers() {
        val response = apiRequest { service.getUsers(0) }
        users.postValue(response)
    }

    private fun isFetchNeeded(lastSavedTime: String): Boolean {
        return timediff(lastSavedTime, now()) > MINIMUM_INTERVAL
    }


    //fun getUsers() = userDao.getUsers()
    //fun getUser(userId: Long) = userDao.getUser(userId)
}