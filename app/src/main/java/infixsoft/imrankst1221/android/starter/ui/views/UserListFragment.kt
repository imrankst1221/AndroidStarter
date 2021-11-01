package infixsoft.imrankst1221.android.starter.ui.views
/**
 * @author imran.choudhury
 * 1/11/21
 *
 * UserListFragment UI fir user list
 */

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.base.BaseFragment
import infixsoft.imrankst1221.android.starter.databinding.FragmentUserListBinding
import infixsoft.imrankst1221.android.starter.ui.adapters.UserAdapter
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.utilities.Constants
import infixsoft.imrankst1221.android.starter.utilities.Coroutines
import infixsoft.imrankst1221.android.starter.utilities.EndlessRecyclerOnScrollListener

class UserListFragment : BaseFragment<FragmentUserListBinding>(){

    override fun setBinding(): FragmentUserListBinding =
        FragmentUserListBinding.inflate(layoutInflater)

    lateinit var mContext: Context
    lateinit var userViewModel: UsersViewModel
    private lateinit var searchView: SearchView
    private lateinit var onScrollListener: EndlessRecyclerOnScrollListener
    private var userAdapter: UserAdapter = UserAdapter()

    // network failed for network flag
    private var waitingForNetwork = false
    // unregister observer
    private var isFragmentAvailable = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel
        mContext = (activity as MainActivity).applicationContext
        setupUI()
        setupRecyclerView()
        setupObserver()
        isFragmentAvailable = true
    }

    private fun setupUI() {
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // skeletons view start
        binding.shimmerViewContainer.startShimmer()

        // on click
        binding.itemNoInternet.btnRetry.setOnClickListener {
            loadMoreUsers()
        }
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            loadMoreUsers()
        }

        // on list scroll load more
        onScrollListener = object : EndlessRecyclerOnScrollListener(Constants.QUERY_PER_PAGE) {
            override fun onLoadMore() {
                loadMoreUsers()
            }
        }
    }

    // init recycler view
    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(onScrollListener)
        }
        userAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("user", it)
            }
            findNavController().navigate(
                R.id.action_userListFragment_to_userDetailsFragment,
                bundle
            )
        }
    }

    // load more data
    private fun loadMoreUsers(){
        Coroutines.main {
            binding.progressBar.visibility = View.VISIBLE
            userViewModel.loadMoreUsers()
        }
    }

    private fun setupObserver() = Coroutines.main {
        // user list data change observer
        userViewModel.getUserList().observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                loadMoreUsers()
            }else{
                userAdapter.submitList(it)
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.hideShimmer()
                binding.itemNoInternet.root.visibility = View.GONE
                binding.itemErrorMessage.root.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        })

        // no internet n failed observer
        userViewModel.onNoInternetFailed().observe(viewLifecycleOwner, {
            if (it){
                binding.itemNoInternet.root.visibility = View.VISIBLE
            }
        })

        // wait for internet observer
        userViewModel.onUserLoadFailed().observe(viewLifecycleOwner, {
            waitingForNetwork = it
        })

        // network observer
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (waitingForNetwork && isFragmentAvailable){
                    loadMoreUsers()
                }
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                activity?.runOnUiThread {
                    binding.itemNoInternet.root.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnSearchClickListener {
            searchView.maxWidth = android.R.attr.width
        }

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            searchView.maxWidth = 0
            true
        }

        val searchPlate =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = getString(R.string.search)
        val searchPlateView: View =
            searchView.findViewById(androidx.appcompat.R.id.search_plate)

        // toolbar search listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.isEmpty()){
                        userAdapter.filter.filter("")
                    }else {
                        userAdapter.filter.filter(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (query.isEmpty()){
                        userAdapter.filter.filter("")
                    }else {
                        userAdapter.filter.filter(query)
                    }
                }
                return false
            }
        })

        activity?.let {
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    android.R.color.transparent
                )
            )
            val searchManager =
                it.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(it.componentName))
        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        isFragmentAvailable = false
        super.onDestroyView()
    }
}