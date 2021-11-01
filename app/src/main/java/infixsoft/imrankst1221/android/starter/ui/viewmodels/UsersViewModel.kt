package infixsoft.imrankst1221.android.starter.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infixsoft.imrankst1221.android.starter.data.model.UserNote
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import javax.inject.Inject

/**
 * @author imran.choudhury
 * 4/10/21
 *
 */

@HiltViewModel
class UsersViewModel @Inject internal constructor(
        private val userRepository: UserRepository
    ) : ViewModel() {
    // provide user list
    suspend fun getUserList() =  userRepository.getUsers()
    // read more user list data from server
    suspend fun loadMoreUsers() =  userRepository.loadMoreUsers()
    // read user details from DB
    suspend fun getUserDetails(userName: String) = userRepository.getUserDetails(userName)
    // read user details from DB/server
    suspend fun fetchUserDetails(userName: String) = userRepository.fetchUserDetails(userName)
    // store user note
    suspend fun storeUserNote(note: UserNote) = userRepository.storeUserNote(note)
    // on network failed
    suspend fun onNoInternetFailed() = userRepository.onNoInternetFailed()
    // on user list error
    suspend fun onUserLoadFailed() = userRepository.onUserLoadFailed()
    // on user details error
    suspend fun onUserDetailsLoadFailed() = userRepository.onUserDetailsLoadFailed()
}