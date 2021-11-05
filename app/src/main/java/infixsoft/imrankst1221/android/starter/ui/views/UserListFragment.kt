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
import infixsoft.imrankst1221.android.starter.utilities.Coroutines
import androidx.recyclerview.widget.RecyclerView

class UserListFragment : BaseFragment<FragmentUserListBinding>(){

    override fun setBinding(): FragmentUserListBinding =
        FragmentUserListBinding.inflate(layoutInflater)

    lateinit var mContext: Context
    lateinit var userViewModel: UsersViewModel
    private lateinit var searchView: SearchView
    private var isScrollLoading = true
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var userAdapter: UserAdapter = UserAdapter()

    // network failed for network flag
    private var waitingForNetwork = false
    // unregister observer
    private var isFragmentAvailable = true
    private var isObserverInit = true
    private var searchQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel
        mContext = (activity as MainActivity).applicationContext
        setupUI()
        setupRecyclerView()
        isFragmentAvailable = true
    }

    private fun setupUI() {
        if(isObserverInit) {
            isObserverInit = false
            setupObserver()
            // skeletons view start
            showShimmer()
        } else{
            hideShimmer()
        }

        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // on click
        binding.itemNoInternet.btnRetry.setOnClickListener {
            loadMoreUsers()
        }
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            loadMoreUsers()
        }
    }

    // init recycler view
    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
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

        // on list scroll load more
        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = binding.rvUsers.layoutManager?.childCount ?: 0
                    totalItemCount = binding.rvUsers.layoutManager?.itemCount ?: 0
                    pastVisibleItems = (binding.rvUsers.layoutManager
                            as LinearLayoutManager?)?.findFirstVisibleItemPosition() ?: 0
                    if (isScrollLoading && searchView.isIconified) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            isScrollLoading = false
                            loadMoreUsers()
                        }
                    }
                }
            }
        })
    }

    // load more data
    private fun loadMoreUsers(){
        Coroutines.main {
            binding.progressBar.visibility = View.VISIBLE
            userViewModel.loadMoreUsers()
        }
    }

    private fun showShimmer(){
        binding.shimmerViewContainer.startShimmer()
    }
    private fun hideShimmer(){
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.hideShimmer()
    }

    private fun setupObserver() = Coroutines.main {
        // user list data change observer
        userViewModel.getUserList().observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                loadMoreUsers()
            }else{
                userAdapter.submitList(it)
                isScrollLoading = true
                hideShimmer()
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
                        searchQuery = ""
                        userAdapter.filter.filter("")
                    }else {
                        searchQuery = query
                        userAdapter.filter.filter(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (query.isEmpty()){
                        searchQuery = ""
                        userAdapter.filter.filter("")
                    }else {
                        searchQuery = query
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

        if(!searchQuery.isNullOrEmpty()){
            searchPlate.setText(searchQuery)
            searchView.isIconified = false
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        isFragmentAvailable = false
        super.onDestroyView()
    }
}