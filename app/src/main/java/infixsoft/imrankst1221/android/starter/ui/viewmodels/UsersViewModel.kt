package infixsoft.imrankst1221.android.starter.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infixsoft.imrankst1221.android.starter.data.repository.UserRepository
import infixsoft.imrankst1221.android.starter.utilities.lazyDeferred
import javax.inject.Inject

/**
 * @author imran.choudhury
 * 4/10/21
 */

@HiltViewModel
class UsersViewModel @Inject internal constructor(
    userRepository: UserRepository) : ViewModel() {
    val users by lazyDeferred {
        userRepository.getUsers()
    }
}