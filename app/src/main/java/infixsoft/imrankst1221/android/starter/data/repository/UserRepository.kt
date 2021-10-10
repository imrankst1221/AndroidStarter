package infixsoft.imrankst1221.android.starter.data.repository

import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import infixsoft.imrankst1221.android.starter.data.api.SafeApiRequest
import infixsoft.imrankst1221.android.starter.data.api.UserApiService
import infixsoft.imrankst1221.android.starter.data.model.*
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
    private val userDetailsDao: UserDetailsDao,
    private val userNoteDao: UserNoteDao,
    private val service: UserApiService): SafeApiRequest(){

    private val users = MutableLiveData<ArrayList<User>>()
    private val userDetails = MutableLiveData<UserDetails>()

    init {
        users.observeForever{
            saveUsers(it)
        }
        userDetails.observeForever{
            saveUserDetails(it)
        }
    }

    private fun saveUsers(users: List<User>) {
        Coroutines.io {
            userDao.insertAll(users)
        }
    }

    private fun saveUserDetails(userDetails: UserDetails){
        Coroutines.io {
            userDetailsDao.insertUserDetails(userDetails)
        }
    }

    suspend fun getUsers(): LiveData<List<User>> {
        return withContext(Dispatchers.IO) {
            userDao.getUsers()
        }
    }

    private suspend fun fetchUsers() {
        val size = userDao.getUsers().value?.size ?: 0
        if (networkHelper.isNetworkConnected() && size <= 0) {
            val response = apiRequest { service.getUsers(size) }
            users.postValue(response)
        }else if (size > 0){
            users.postValue(userDao.getUsers().value!!.toCollection(ArrayList()))
        }
    }

    suspend fun loadMoreUsers() {
        if (networkHelper.isNetworkConnected()) {
            val size = users.value?.size ?: 0
            val response = apiRequest { service.getUsers(size) }
            if (size > 0){
                val newList= users.value
                newList?.addAll(response)
                users.postValue(newList)
            }else{
                users.postValue(response)
            }
        }
    }

    suspend fun getUserDetails(userName: String): LiveData<UserDetails> {
        return withContext(Dispatchers.IO) {
            fetchUserDetails(userName)
            userDetailsDao.getUserDetailsByLogin(userName)
        }
    }

    private suspend fun fetchUserDetails(userName: String) {
        if (networkHelper.isNetworkConnected()) {
            val response = apiRequest { service.getUserDetails(userName) }
            userDetails.postValue(response)
        }
    }

    suspend fun storeUserNote(note: UserNote){
        userNoteDao.insertNote(note)
    }

    private suspend fun getUserNote(userId: Long): UserNote?{
        return userNoteDao.getNote(userId)
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }

    //fun getUsers() = userDao.getUsers()
    //fun getUser(userId: Long) = userDao.getUser(userId)
    //fun getUserDetails(userId: Long) = userDetailsDao.getUserDetails(userId)
    //fun isUserNote(userId: Long) = userDetailsDao.isUserDetailsAvailable(userId)
}