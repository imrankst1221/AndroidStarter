package infixsoft.imrankst1221.android.starter.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.base.BaseFragment
import infixsoft.imrankst1221.android.starter.databinding.FragmentUserListBinding
import infixsoft.imrankst1221.android.starter.ui.adapters.UserAdapter
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.utilities.Coroutines

class UserListFragment : BaseFragment<FragmentUserListBinding>(){

    override fun setBinding(): FragmentUserListBinding =
        FragmentUserListBinding.inflate(layoutInflater)

    lateinit var userViewModel: UsersViewModel
    lateinit var userAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = (activity as MainActivity).userViewModel
        setupRecyclerView()
        //setupUI(view)
        setupObserver()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    private fun setupObserver() = Coroutines.main {
        userViewModel.getUserList().observe(viewLifecycleOwner, Observer {
            userAdapter.differ.submitList(it)
        })
    }
}