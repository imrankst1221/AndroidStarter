package infixsoft.imrankst1221.android.starter.ui.views

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.base.BaseFragment
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserNote
import infixsoft.imrankst1221.android.starter.databinding.FragmentUserDetailsBinding
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.utilities.BLogger
import infixsoft.imrankst1221.android.starter.utilities.Coroutines

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {
    override fun setBinding(): FragmentUserDetailsBinding =
        FragmentUserDetailsBinding.inflate(layoutInflater)

    private val TAG = "---UserDetailsFragment"
    val args: UserDetailsFragmentArgs by navArgs()
    lateinit var userViewModel: UsersViewModel
    lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel

        setupUI(view)
        setupObserver()
    }

    private fun setupUI(view: View) {
        user = args.user
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = user.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userNote = UserNote(user.userId, "test")
        storeNote(userNote)
    }

    private fun storeNote(note: UserNote) = Coroutines.main {
        userViewModel.storeUserNote(note)
    }

    private fun setupObserver() = Coroutines.main {
        userViewModel.getUserDetails(user.login).observe(viewLifecycleOwner, {
            BLogger.d(TAG, ""+it)
        })
    }
}