package com.example.commyproject.activities.main.fragment.home.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.DialogCommentBinding
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.CommentAdapter
import com.example.commyproject.ultil.adapter.PublicFileAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            hideFile = { file, _->

            },
            createContextMenu = { file ->

            },
            onOpenLikeDialog = { file ->

            },
            onClickComment = { file ->
                openCommentDialog(file)
            },
            onItemClick = { file ->

            }
        )
        b.apply {
            listView.adapter = adapter
        }
    }

    private fun postLike(file: FileEntry?, cmt: Comment?, type: EvaluationEntityType, callback: (evaluation: Evaluation) -> Unit) {
        var evaluationEntity: EvaluationEntity
        val id = FileConverter.generateIdByUserId(user._id)
        when(type) {
            EvaluationEntityType.FILE -> {
                evaluationEntity = EvaluationEntity(id, user._id, file!!._id, null, type)
            }
            EvaluationEntityType.COMMENT -> {
                evaluationEntity = EvaluationEntity(id, user._id, cmt!!.idFile, cmt._id, type)
            }
        }
        viewModel.postLike(evaluationEntity) {evaluation ->

            callback(evaluation)
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

//        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.root)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomDialog.setContentView(binding.root)

        val cmtAdapter = CommentAdapter(
            requireContext(),
            user._id,
            file.comments,
            onGoToUserProfile = { cmt ->

            },
            onClickLike = { cmt, callback ->
                postLike(null, cmt, EvaluationEntityType.COMMENT, callback)
            },
            onClickReply = { cmt ->
                viewModel.toId = cmt.idUser
                binding.apply {
                    toUser.visibility = View.VISIBLE
                    cancelReply.visibility = View.VISIBLE
                    toUser.text = getString(R.string.answer_to, cmt.userName)
                }
            },
            onOpenLike = { cmt ->

            }
        )

        binding.listViewComment.adapter = cmtAdapter

        binding.apply {
            // button send comment
            btnSend.setOnClickListener {
                if (binding.inputReply.text.isNotEmpty()) {
                    binding.inputReply.setText("")
                    var content = binding.inputReply.text.toString()
                    val id = FileConverter.generateIdByUserId(file.idUser)
                    if (viewModel.toId != null) content = "Answer @${viewModel.toUserName} $content"
                    val cmt = CommentEntity(id, user._id, viewModel.toId, file._id, content)
                    viewModel.postComment(cmt) {
                        file.comments.add(it)
                        GlobalScope.launch(Dispatchers.Main) {
                            cmtAdapter.notifyDataSetChanged()
                        }
                        viewModel.toId = null
                        viewModel.toUserName = null
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