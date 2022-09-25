package com.example.koin.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.koin.databinding.FragmentUsersBinding
import com.example.domain.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment() {
    private val usersViewModel: UsersViewModel by viewModel()
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UsersAdapter

    companion object {
        fun newInstance() = UsersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
    }

    private fun updateUi() {
        userAdapter = UsersAdapter(arrayListOf(), onUserClick)
        binding.rvUsers.adapter = userAdapter
        usersViewModel.usersList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUsers.visibility = View.VISIBLE
                    result.data?.let {
                        userAdapter.update(it)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUsers.visibility = View.GONE
                    result.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvUsers.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onUserClick: (view: View) -> Unit = {
    }
}