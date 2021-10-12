package infixsoft.imrankst1221.android.starter.ui.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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

    lateinit var mContext: Context
    lateinit var userViewModel: UsersViewModel
    lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel
        mContext = (activity as MainActivity).applicationContext
        setupUI(view)
        setupObserver()
        setupOnClick()
    }

    private fun setupUI(view: View) {
        user = args.user
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = user.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun storeNote(note: UserNote) = Coroutines.main {
        userViewModel.storeUserNote(note)
    }

    private fun setupObserver() = Coroutines.main {
        userViewModel.getUserDetails(user.login).observe(viewLifecycleOwner, { userDetails ->
            if(userDetails == null) {
                Coroutines.main {
                    userViewModel.fetchUserDetails(user.login)
                }
            }else {
                Glide.with(mContext)
                    .load(userDetails.avatarUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.ivUserProfile)

                binding.tvFollower.text = userDetails.followers.toString()
                binding.tvFollowing.text = userDetails.following.toString()
                binding.tvName.text = userDetails.name
                binding.tvCompany.text = userDetails.company
                binding.tvBlog.text = userDetails.blog
                binding.tvNote.setText(user.userNote ?: "")
            }
        })

        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (userViewModel.isUserDetailsLoadFailed()){
                    Coroutines.main {
                        userViewModel.fetchUserDetails(user.login)
                    }
                }
            }
        })
    }

    private fun setupOnClick(){
        binding.btnSave.setOnClickListener {
            if (!binding.tvNote.text.isNullOrEmpty()) {
                val userNote = UserNote(user.userId, binding.tvNote.text.toString())
                storeNote(userNote)
                Toast.makeText(mContext, "Save success!", Toast.LENGTH_LONG).show()
            }
        }
    }
}