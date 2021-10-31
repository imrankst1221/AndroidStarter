package infixsoft.imrankst1221.android.starter.data.repository

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

    private val isInternetFailed = MutableLiveData<Boolean>()
    private val isUserLoadFailed = MutableLiveData<Boolean>()
    private val isUserDetailsLoadFailed = MutableLiveData<Boolean>()
    private val users = MutableLiveData<ArrayList<User>>()
    private val userDetails = MutableLiveData<UserDetails>()

    init {
        isInternetFailed.value = false
        isUserLoadFailed.value = false
        isUserDetailsLoadFailed.value = false

        users.observeForever{
            saveUsers(it)
        }
        userDetails.observeForever{
            saveUserDetails(it)
        }
    }

    private fun saveUsers(users: ArrayList<User>) {
        Coroutines.io {
            userDao.insertAll(users)
        }
    }

    private fun saveUserDetails(userDetails: UserDetails){
        Coroutines.io {
            userDetailsDao.insertUserDetails(userDetails)
        }
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        Coroutines.io {
            val usersWithNote = userDao.getUsersWithNote()
            users.postValue(ArrayList(usersWithNote))
        }
        return users
    }

    suspend fun loadMoreUsers() {
        if (networkHelper.isNetworkConnected()) {
            val size = users.value?.size ?: 0
            val response = apiRequest { service.getUsers(size) }
            isUserLoadFailed.value = false
            users.postValue(response)
        }else{
            isInternetFailed.value = true
            isUserLoadFailed.value = true
        }
    }

    suspend fun getUserDetails(userName: String): LiveData<UserDetails> {
        return withContext(Dispatchers.IO) {
            userDetailsDao.getUserDetailsByLogin(userName)
        }
    }

    suspend fun fetchUserDetails(userName: String) {
        if (networkHelper.isNetworkConnected()) {
            val response = apiRequest { service.getUserDetails(userName) }
            userDetails.postValue(response)
            isUserDetailsLoadFailed.value = false
        }else{
            isInternetFailed.value = true
            isUserDetailsLoadFailed.value = true
        }
    }

    suspend fun storeUserNote(note: UserNote){
        userNoteDao.insertNote(note)
    }

    private suspend fun getUserNote(userId: Long): UserNote?{
        return userNoteDao.getNote(userId)
    }

    suspend fun onNoInternetFailed(): LiveData<Boolean>{
        return withContext(Dispatchers.IO) {
            isInternetFailed
        }
    }

    suspend fun onUserLoadFailed(): LiveData<Boolean>{
        return withContext(Dispatchers.IO) {
            isUserLoadFailed
        }
    }

    suspend fun onUserDetailsLoadFailed(): LiveData<Boolean>{
        return withContext(Dispatchers.IO) {
            isUserDetailsLoadFailed
        }
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }
}