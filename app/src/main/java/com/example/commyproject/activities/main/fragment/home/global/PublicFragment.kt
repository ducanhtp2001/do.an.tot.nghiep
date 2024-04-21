package com.example.commyproject.activities.main.fragment.home.global

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.PublicFileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicFragment : Fragment() {
    private lateinit var b: FragmentPublicBinding
    private lateinit var viewModel: PublicFragmentViewModel
    private lateinit var user: User
    private lateinit var adapter: PublicFileAdapter
    private lateinit var list: MutableList<FileEntry>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentPublicBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PublicFragmentViewModel::class.java]
        user = viewModel.getUser()

        initData()
        initView()
        initEvent()
        initObserver()

        return b.root
    }

    private fun initObserver() {

    }

    private fun initEvent() {

    }

    private fun initView() {
        adapter = PublicFileAdapter(requireContext(), 
            list, 
            getMoreComments = {
                getMoreComment()
            },
            sendComment = {
                sendComment(user._id)
            },
            createContextMenu = {
                createContextMenu()
            },
            sendUpvote = {
                sendUpvote()
            }
            )
        b.apply {
            listView.adapter = adapter
        }
    }

    private fun sendComment(_id: String){

    }

    private fun sendUpvote() {

    }

    private fun createContextMenu() {

    }


    private fun getMoreComment() {

    }

    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPublicFile()
        list = mutableListOf()
        viewModel.list.value?.let { list.addAll(it) }
    }

}