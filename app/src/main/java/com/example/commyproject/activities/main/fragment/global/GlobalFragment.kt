package com.example.commyproject.activities.main.fragment.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileViewModel
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.databinding.ActivityProfileBinding
import com.example.commyproject.databinding.FragmentGlobalBinding
import com.example.commyproject.ultil.adapter.GlobalFileRCAdapter
import com.example.commyproject.ultil.adapter.KeyRecommendAdapter
import com.example.commyproject.ultil.adapter.PeopleRCAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalFragment : Fragment() {
    private lateinit var viewModel: GlobalFragmentViewModel
    private lateinit var b: FragmentGlobalBinding
    private lateinit var list: MutableList<FileEntry>
    private lateinit var keyRecommendAdapter: KeyRecommendAdapter
    private lateinit var fileAdapter: GlobalFileRCAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentGlobalBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GlobalFragmentViewModel::class.java]

        initData()
        initObserver()
        return b.root
    }

    private fun initObserver() {

    }

    private fun initData() {

    }

}