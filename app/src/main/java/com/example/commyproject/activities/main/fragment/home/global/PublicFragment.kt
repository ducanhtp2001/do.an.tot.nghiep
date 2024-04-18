package com.example.commyproject.activities.main.fragment.home.global

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.commyproject.R
import com.example.commyproject.activities.main.fragment.home.local.LocalFragmentViewModel
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentLocalBinding
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.PrivateFileAdapter
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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public, container, false)
    }

}