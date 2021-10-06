package infixsoft.imrankst1221.android.starter.data.repository

import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infixsoft.imrankst1221.android.starter.data.api.SafeApiRequest
import infixsoft.imrankst1221.android.starter.data.api.UserApiService
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import infixsoft.imrankst1221.android.starter.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author imran.choudhury
 * 30/9/21
 */

@Singleton
class UserRepository @Inject constructor(
    private val networkHelper: NetworkHelper,
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
            userDao.insertAll(users)
        }
    }

    suspend fun getUsers(): LiveData<List<User>> {
        return withContext(Dispatchers.IO) {
            fetchUsers()
            userDao.getUsers()
        }
    }


    private suspend fun fetchUsers() {
        if (networkHelper.isNetworkConnected()) {
            val size = users.value?.size ?: 0
            val response = apiRequest { service.getUsers(size) }
            users.postValue(response)
        }
    }

    //fun getUsers() = userDao.getUsers()
    //fun getUser(userId: Long) = userDao.getUser(userId)
}