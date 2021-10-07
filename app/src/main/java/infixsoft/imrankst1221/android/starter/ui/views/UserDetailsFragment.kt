package infixsoft.imrankst1221.android.starter.ui.views

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.base.BaseFragment
import infixsoft.imrankst1221.android.starter.databinding.FragmentUserDetailsBinding
import infixsoft.imrankst1221.android.starter.utilities.BLogger

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {
    override fun setBinding(): FragmentUserDetailsBinding =
        FragmentUserDetailsBinding.inflate(layoutInflater)

    private val TAG = "---UserDetailsFragment"
    val args: UserDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
    }

    private fun setupUI(view: View) {
        val user = args.user
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = user.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}