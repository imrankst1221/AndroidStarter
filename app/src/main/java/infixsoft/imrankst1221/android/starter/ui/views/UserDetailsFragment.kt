package infixsoft.imrankst1221.android.starter.ui.views
/**
 * @author imran.choudhury
 * 1/11/21
 *
 * UserDetailsFragment UI for user details
 */

import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.*
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
import infixsoft.imrankst1221.android.starter.utilities.Coroutines
import kotlin.math.abs

import android.view.ViewTreeObserver.OnGlobalLayoutListener
import infixsoft.imrankst1221.android.starter.utilities.Extensions.scrollToBottomWithoutFocusChange

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>(), OnGlobalLayoutListener {
    override fun setBinding(): FragmentUserDetailsBinding =
        FragmentUserDetailsBinding.inflate(layoutInflater)

    private val TAG = "---UserDetailsFragment"
    val args: UserDetailsFragmentArgs by navArgs()

    lateinit var mRootView: View
    lateinit var mContext: Context
    lateinit var userViewModel: UsersViewModel
    lateinit var user: User

    // unregister observer
    private var isFragmentAvailable = true
    // network failed flag
    private var waitingForNetwork = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel
        mContext = (activity as MainActivity).applicationContext
        mRootView = view
        setupUI(view)
        setupObserver()
        isFragmentAvailable = true
    }

    private fun setupUI(view: View) {
        user = args.user
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = user.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // skeletons view start
        binding.shimmerViewContainer.startShimmer()

        // on click
        binding.itemNoInternet.btnRetry.setOnClickListener {
            fetchUserDetails(user.login)
        }
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            fetchUserDetails(user.login)
        }
        binding.btnSave.setOnClickListener {
            if (!binding.tvNote.text.isNullOrEmpty()) {
                val userNote = UserNote(user.userId, binding.tvNote.text.toString())
                storeNote(userNote)
                Toast.makeText(mContext, "Save success!", Toast.LENGTH_LONG).show()
            }
        }
    }

    // store user note in DB
    private fun storeNote(note: UserNote) = Coroutines.main {
        userViewModel.storeUserNote(note)
    }

    // fetch user not from API
    private fun fetchUserDetails(userName: String){
        Coroutines.main {
            userViewModel.fetchUserDetails(userName)
        }
    }

    private fun setupObserver() = Coroutines.main {
        // keyboard on/off auto scroll observer
        binding.scrollRoot.viewTreeObserver.addOnGlobalLayoutListener(this)

        // user details data change observer
        userViewModel.getUserDetails(user.login).observe(viewLifecycleOwner, { userDetails ->
            if(userDetails == null) {
                fetchUserDetails(user.login)
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

                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.hideShimmer()
                binding.itemNoInternet.root.visibility = View.GONE
                binding.itemErrorMessage.root.visibility = View.GONE
            }
        })

        // no internet API failed observer
        userViewModel.onNoInternetFailed().observe(viewLifecycleOwner, {
            if (it){
                binding.itemNoInternet.root.visibility = View.VISIBLE
            }
        })

        // wait for internet observer
        userViewModel.onUserDetailsLoadFailed().observe(viewLifecycleOwner, {
            waitingForNetwork = it
        })

        // network observer
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (waitingForNetwork){
                    fetchUserDetails(user.login)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                activity?.runOnUiThread {
                    //binding.itemNoInternet.root.visibility = View.VISIBLE
                }
            }
        })
    }

    // keyboard on/off layout change listener
    override fun onGlobalLayout() {
        if(isFragmentAvailable) {
            val r = Rect()
            mRootView.getWindowVisibleDisplayFrame(r)
            if (abs(mRootView.rootView.height - (r.bottom - r.top)) > 100) {
                binding.scrollRoot.scrollToBottomWithoutFocusChange()
            }
        }else{
            mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onDestroyView() {
        isFragmentAvailable = false
        super.onDestroyView()
    }
}