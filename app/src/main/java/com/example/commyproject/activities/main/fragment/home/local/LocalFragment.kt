package com.example.commyproject.activities.main.fragment.home.local

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentLocalBinding
import com.example.commyproject.ultil.adapter.PrivateFileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalFragment : Fragment() {

    private lateinit var b: FragmentLocalBinding
    private lateinit var viewModel: LocalFragmentViewModel
    private lateinit var user: User
    private lateinit var adapter: PrivateFileAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLocalBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[LocalFragmentViewModel::class.java]


        initEvent()
        initData()
        initObserver()

        return b.root
    }

    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPrivateFile()
    }

    private fun initObserver() {
        viewModel.list.observe(requireActivity()) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun initEvent() {
        adapter = PrivateFileAdapter(requireContext(), viewModel.list.value!!)
        b.apply {
            listView.adapter = adapter
        }
    }

}