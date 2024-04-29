package com.example.commyproject.activities.main.fragment.home.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.activities.bottomsheetdialog.showCommentDialog
import com.example.commyproject.activities.bottomsheetdialog.showContextMenuDialog
import com.example.commyproject.activities.bottomsheetdialog.showFileDetailDialog
import com.example.commyproject.activities.bottomsheetdialog.showLikeDialog
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.PublicFileAdapter
import com.example.commyproject.ultil.showToast
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
        viewModel.apply {
            list.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    this@PublicFragment.list.clear()
                    this@PublicFragment.list.addAll(it)
                    this@PublicFragment.adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initEvent() {
    }

    private fun initView() {
        adapter = PublicFileAdapter(
            requireContext(),
            user._id,
            list,
            hideFile = { file, _ ->

            },
            createContextMenu = { file ->
                showContextMenuDialog(
                    file,
                    updateState = { response, mFile ->
                        requireContext().showToast(response.msg)
                        list.removeIf {
                            it._id == mFile._id
                        }
                        requireActivity().runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    },
                )
            },
            onOpenLikeDialog = { file ->
                showLikeDialog(file, null)
            },
            onClickComment = { file ->
                showCommentDialog(file)
            },
            onItemClick = { file, updateLike ->
                showFileDetailDialog(file,
                    updateState = { response, mFile ->
                        this.requireContext().showToast(response.msg)
                        this.list.removeIf { it._id == mFile._id }
                        this.requireActivity().runOnUiThread {
                            this.adapter.notifyDataSetChanged()
                        }
                    },
                    updateLike = { evaluation ->
                        updateLike(evaluation)
                        val position = list.indexOf(file)
                        if (list[position].likes.any { it.idUser == evaluation.idUser }) {
                            list[position].likes.removeIf{ it.idUser == evaluation.idUser }
                        } else {
                            list[position].likes.add(evaluation)
                        }
                    })
            }
        )
        b.apply {
            listView.adapter = adapter
        }
    }

    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPublicFile()
        list = mutableListOf()
        viewModel.list.value?.let { list.addAll(it) }
    }

}