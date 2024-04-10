package com.example.commyproject.activities.main.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.activities.main.fragment.home.global.PublicFragment
import com.example.commyproject.activities.main.fragment.home.local.LocalFragment
import com.example.commyproject.databinding.FragmentHomeBinding
import com.example.commyproject.ultil.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var vpAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        initView()
        initEvent()

        return b.root
    }

    private fun initEvent() {
        b.apply {
            fab.setOnClickListener {
                // open gallery, choose file and upload
            }
        }
    }

    private fun initView() {
        vpAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        vpAdapter.add(LocalFragment(), "Private")
        vpAdapter.add(PublicFragment(), "Publish")

        b.viewPager.adapter = vpAdapter
        b.tabLayout.setupWithViewPager(b.viewPager)
    }
}