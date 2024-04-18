package com.example.commyproject.activities.main.fragment.home.local

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentLocalBinding
import com.example.commyproject.ultil.adapter.PrivateFileAdapter
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalFragment : Fragment() {

    private lateinit var b: FragmentLocalBinding
    private lateinit var viewModel: LocalFragmentViewModel
    private lateinit var user: User
    private lateinit var adapter: PrivateFileAdapter
    private lateinit var list: MutableList<FileEntry>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLocalBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[LocalFragmentViewModel::class.java]

//        Log.d("testing", "onCreate")

        initData()
        initView()
        initEvent()
        initObserver()

        return b.root
    }

    private fun initView() {
        adapter = PrivateFileAdapter(requireContext(), list)
        b.apply {
            listView.adapter = adapter
        }
    }

    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPrivateFile()
        list = mutableListOf()
        viewModel.list.value?.let { list.addAll(it) }
    }

    private fun initObserver() {
        viewModel.list.observe(requireActivity()) {
            if (viewModel.list.value!!.isNotEmpty()) {
//                Log.d("testing", "lst: ${viewModel.list.value?.get(0)?.summaryText}")
                list.clear()
                list.addAll(viewModel.list.value!!)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initEvent() {

        b.listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Log.d("testing", "Item clicked at position: $position")
            b.listView.context.showToast(list[position]._id)
        }
    }

}