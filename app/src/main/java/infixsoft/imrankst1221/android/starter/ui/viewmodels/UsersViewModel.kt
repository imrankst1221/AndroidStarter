package infixsoft.imrankst1221.android.starter.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infixsoft.imrankst1221.android.starter.data.model.UserNote
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import javax.inject.Inject

/**
 * @author imran.choudhury
 * 4/10/21
 */

@HiltViewModel
class UsersViewModel @Inject internal constructor(
        private val userRepository: UserRepository
    ) : ViewModel() {
    suspend fun getUserList() =  userRepository.getUsers()
    suspend fun loadMoreUsers() =  userRepository.loadMoreUsers()
    suspend fun getUserDetails(userName: String) = userRepository.getUserDetails(userName)
    suspend fun fetchUserDetails(userName: String) = userRepository.fetchUserDetails(userName)
    suspend fun storeUserNote(note: UserNote) = userRepository.storeUserNote(note)

    suspend fun onNoInternetFailed() = userRepository.onNoInternetFailed()
    suspend fun onUserLoadFailed() = userRepository.onUserLoadFailed()
    suspend fun onUserDetailsLoadFailed() = userRepository.onUserDetailsLoadFailed()
}