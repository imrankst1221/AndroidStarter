package infixsoft.imrankst1221.android.starter.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infixsoft.imrankst1221.android.starter.data.model.UserNote
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import infixsoft.imrankst1221.android.starter.utilities.lazyDeferred
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
    fun loadMoreUsers() =  lazyDeferred { userRepository.loadMoreUsers()}

    suspend fun getUserDetails(userName: String)
            = userRepository.getUserDetails(userName)
    fun storeUserNote(note: UserNote) =  lazyDeferred {userRepository.storeUserNote(note)}
}