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
import com.example.commyproject.databinding.DialogLikeBinding
import com.example.commyproject.databinding.FragmentPublicBinding
import com.example.commyproject.ultil.adapter.CommentAdapter
import com.example.commyproject.ultil.adapter.LikeAdapter
import com.example.commyproject.ultil.adapter.PublicFileAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.getNavigationBarHeight
import com.example.commyproject.ultil.getStatusBarHeight
import com.example.commyproject.ultil.showToast
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
            hideFile = { file, _ ->

            },
            createContextMenu = { file ->

            },
            onOpenLikeDialog = { file ->
                openLikeDialog(file, null)
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

    private fun postLike(
        file: FileEntry?,
        cmt: Comment?,
        type: EvaluationEntityType,
        callback: (evaluation: Evaluation) -> Unit
    ) {
        var evaluationEntity: EvaluationEntity
        val id = FileConverter.generateIdByUserId(user._id)
        evaluationEntity = when (type) {
            EvaluationEntityType.FILE -> {
                EvaluationEntity(id, user._id, file!!._id, null, type)
            }

            EvaluationEntityType.COMMENT -> {
                EvaluationEntity(id, user._id, cmt!!.idFile, cmt._id, type)
            }
        }
        viewModel.postLike(evaluationEntity) { evaluation ->

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
                goToUserProfile(cmt.idUser)
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
                openLikeDialog(null, cmt)
            }
        )

        binding.listViewComment.adapter = cmtAdapter

        binding.apply {
            // button send comment
            btnSend.setOnClickListener {
                if (binding.inputReply.text.isNotEmpty()) {
                    var content = binding.inputReply.text.toString()
                    binding.inputReply.setText("")
                    val id = FileConverter.generateIdByUserId(file.idUser)
                    if (viewModel.toId != null) content = "Answer @${viewModel.toUserName} $content"
                    val cmt = CommentEntity(id, user._id, viewModel.toId, file._id, content)
                    viewModel.postComment(cmt) {
                        file.comments.add(it)
                        requireActivity().runOnUiThread {
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
                openLikeDialog(file, null)
            }
        }

        bottomDialog.show()
    }

    private fun openLikeDialog(file: FileEntry?, cmt: Comment?) {

        requireContext().showToast("Open like dialog")

        val bottomDialog = BottomSheetDialog(requireContext())
        val binding = DialogLikeBinding.inflate(layoutInflater, null, false)

        val windowHeight = requireActivity().window.decorView.height
        val statusBarHeight = getStatusBarHeight()
        val navigationBarHeight = getNavigationBarHeight()
        val usableHeight = windowHeight - statusBarHeight - navigationBarHeight
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            usableHeight
        )

        bottomDialog.setContentView(binding.root)

        val likeList = file?.likes ?: cmt!!.likes

        val likeAdapter = LikeAdapter(
            requireContext(),
            likeList ?: mutableListOf(),
            openProfile = { evaluation ->
                goToUserProfile(evaluation.idUser)
            }
        )

        binding.apply {
            txtLikeCount.text = likeList!!.size.toString()
            listViewLike.adapter = likeAdapter
        }

        bottomDialog.show()
    }

    private fun goToUserProfile(idUser: String) {
        requireContext().showToast("Open user profile")
    }

    private fun initData() {
        user = viewModel.getUser()
        viewModel.getPublicFile()
        list = mutableListOf()
        viewModel.list.value?.let { list.addAll(it) }
    }

}