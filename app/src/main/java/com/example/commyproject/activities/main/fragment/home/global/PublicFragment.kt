package com.example.commyproject.activities.main.fragment.home.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.DialogCommentBinding
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.CommentAdapter
import com.example.commyproject.ultil.adapter.PublicFileAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
            hideFile = { file ->
                requireContext().showToast("hide file")
            },
            createContextMenu = {file ->
                requireContext().showToast("menu")
            },
            onClickLike = { file ->
                requireContext().showToast("like")
            },
            onClickComment = { file ->
                openCommentDialog(file)
            },
            onItemClick = {file ->
                requireContext().showToast("see detail")
            }
        )
        b.apply {
            listView.adapter = adapter
        }
    }

    private fun openCommentDialog(file: FileEntry) {

        val bottomDialog = BottomSheetDialog(requireContext())
        val binding = DialogCommentBinding.inflate(layoutInflater, null, false)

        val windowHeight = requireActivity().window.decorView.height
        val statusBarHeight = getStatusBarHeight()
        val navigationBarHeight = getNavigationBarHeight()
        val usableHeight = windowHeight - statusBarHeight - navigationBarHeight
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            usableHeight
        )

        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomDialog.setContentView(binding.root)

        binding.apply {
            btnSend.setOnClickListener {
                if (binding.inputReply.text.isNotEmpty()) {
                    val content = binding.inputReply.text.toString()
                    val id = FileConverter.generateIdByUserId(file.idUser)
                    val cmt = CommentEntity(id, file.idUser, viewModel.toId, file._id, content)
                    viewModel.postComment(cmt) {
                        file.comments.add(it)
                        viewModel.toId = null
                        binding.inputReply.setText("")
                    }
                }
            }

            cancelReply.setOnClickListener {
                it.visibility = View.GONE
                binding.toUser.visibility = View.GONE
                viewModel.toId = null
            }

            txtLikeCount.text = file.likes.size.toString()

            like.setOnClickListener {
                openLikeDialog()
            }

            val cmtAdapter = CommentAdapter()
        }

        bottomDialog.show()
    }

    private fun openLikeDialog() {

    }


    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun getNavigationBarHeight(): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }


    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPublicFile()
        list = mutableListOf()
        viewModel.list.value?.let { list.addAll(it) }
    }

}