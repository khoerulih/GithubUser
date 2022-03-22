package com.example.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.responses.ListUser
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.adapters.ListUserAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUserFollow.layoutManager = layoutManager
        binding.rvUserFollow.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUserFollow.addItemDecoration(itemDecoration)

        detailViewModel.listFollow.observe(viewLifecycleOwner){ listFollow ->
            setListFollowData(listFollow)
        }

        detailViewModel.isFollowDataNullOrBlank.observe(viewLifecycleOwner){
            showEmptyMessage(it)
        }

        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            0 -> {
                detailViewModel.showListFollower(username)
            }
            1 -> {
                detailViewModel.showListFollowing(username)
            }
        }

    }

    private fun setListFollowData(listFollow: List<ListUser>) {
        val listFollowAdapter = ListUserAdapter(listFollow as ArrayList<ListUser>)
        binding.rvUserFollow.adapter = listFollowAdapter
    }

    private fun showEmptyMessage(isData: Boolean) {
        binding.emptyMessage.visibility = if (isData) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_SECTION_NUMBER = "arg_section_number"
    }
}