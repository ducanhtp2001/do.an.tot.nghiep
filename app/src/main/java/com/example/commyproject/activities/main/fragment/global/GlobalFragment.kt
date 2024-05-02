package com.example.commyproject.activities.main.fragment.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.data.model.GlobalFile
import com.example.commyproject.databinding.FragmentGlobalBinding
import com.example.commyproject.ultil.adapter.GlobalFileRCAdapter
import com.example.commyproject.ultil.adapter.KeyRecommendAdapter
import com.example.commyproject.ultil.adapter.ProfileFileRCAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalFragment : Fragment() {
    private lateinit var viewModel: GlobalFragmentViewModel
    private lateinit var b: FragmentGlobalBinding
    private lateinit var listFile: MutableList<GlobalFile>
    private lateinit var listkey: List<String>
    private lateinit var keyRecommendAdapter: KeyRecommendAdapter
    private lateinit var keyAdapter: KeyRecommendAdapter
    private lateinit var fileAdapter: GlobalFileRCAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentGlobalBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GlobalFragmentViewModel::class.java]

        initData()
        initView()
        initObserver()
        return b.root
    }

    private fun initView() {
        keyAdapter = KeyRecommendAdapter(requireContext()) {
            // search by new keyword
        }
        b.apply {
            listKeyRecommend.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            listKeyRecommend.adapter = keyAdapter
        }
    }

    private fun initObserver() {

    }

    private fun initData() {

    }

}