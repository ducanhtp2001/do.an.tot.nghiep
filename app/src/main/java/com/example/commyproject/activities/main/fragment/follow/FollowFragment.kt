package com.example.commyproject.activities.main.fragment.follow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.R
import com.example.commyproject.activities.bottomsheetdialog.postLike
import com.example.commyproject.activities.bottomsheetdialog.showCommentDialog
import com.example.commyproject.activities.bottomsheetdialog.showContextMenuDialog
import com.example.commyproject.activities.bottomsheetdialog.showFileDetailDialog
import com.example.commyproject.activities.bottomsheetdialog.showLikeDialog
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.databinding.FragmentFollowBinding
import com.example.commyproject.ultil.adapter.GlobalFileRCAdapter
import com.example.commyproject.ultil.adapter.PeopleRCAdapter
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : Fragment() {
    private lateinit var b: FragmentFollowBinding
    private lateinit var viewModel: FollowFragmentViewModel
    private lateinit var followList: MutableList<FollowerResponse>
    private lateinit var followAdapter: PeopleRCAdapter
    private lateinit var fileList: MutableList<FileEntry>
    private lateinit var fileAdapter: GlobalFileRCAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentFollowBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[FollowFragmentViewModel::class.java]

        initData()
        initView()
        initObserver()
        initEvent()

        return b.root
    }

    private fun initEvent() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.apply {
            followList.observe(requireActivity()) {
                if (it?.isNotEmpty() == true) {
                    this@FollowFragment.followList.clear()
                    this@FollowFragment.followList.addAll(it)
                    followAdapter.notifyDataSetChanged()
                }
            }

            fileList.observe(requireActivity()) {
                this@FollowFragment.fileList.clear()
                this@FollowFragment.fileList.addAll(it)
                fileAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.apply {
            followList.removeObservers(requireActivity())
            fileList.removeObservers(requireActivity())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        followAdapter = PeopleRCAdapter(requireActivity(), R.layout.item_people_small, followList)
        b.apply {
            listFollows.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            listFollows.adapter = followAdapter
        }

        fileAdapter = GlobalFileRCAdapter(
            requireActivity(),
            viewModel.user,
            fileList,
            createContextMenu = { file ->
                requireActivity().showContextMenuDialog(
                    file,
                    updateState = { response, mFile ->
                        requireActivity().showToast(response.msg)
                        this@FollowFragment.fileList.removeIf {
                            it._id == mFile._id
                        }
                        requireActivity().runOnUiThread {
                            fileAdapter.notifyDataSetChanged()
                        }
                    },
                    onDelete = { fileId ->
                        requireActivity().runOnUiThread {
                            fileList.removeIf { it._id == fileId }
                            fileAdapter.notifyDataSetChanged()
                        }

                    }
                )
            },
            hideFile = { file, callback ->
            },
            onOpenLikeDialog = { file ->
                requireActivity().showLikeDialog(file, null)
            },
            onOpenCommentDialog = { file ->
                requireActivity().showCommentDialog(file)
            },
            openFileDetail = { file, updateLike ->
                requireActivity().showFileDetailDialog(file,
                    updateState = { response, mFile ->
                        requireActivity().showToast(response.msg)
                        this@FollowFragment.fileList.removeIf { it._id == mFile._id }
                        requireActivity().runOnUiThread {
                            this@FollowFragment.fileAdapter.notifyDataSetChanged()
                        }
                    },
                    updateLike = { evaluation ->
                        updateLike(evaluation)
                        val position = this@FollowFragment.fileList.indexOf(file)
                        if (this@FollowFragment.fileList[position].likes.any { it.idUser == evaluation.idUser }) {
                            this@FollowFragment.fileList[position].likes.removeIf { it.idUser == evaluation.idUser }
                        } else {
                            this@FollowFragment.fileList[position].likes.add(evaluation)
                        }
                    },
                    onDelete = { fileId ->
                        requireActivity().runOnUiThread {
                            fileList.removeIf { it._id == fileId }
                            fileAdapter.notifyDataSetChanged()
                        }
                    }
                )
            },
            onLike = { file, callback ->
                postLike(
                    file,
                    null,
                    EvaluationEntityType.FILE,
                    viewModel.user._id,
                    viewModel
                ) { evaluation ->
                    callback(evaluation)
                }
            },
            onFollow = { file, callback ->

            }
        )
        b.apply {
            listFile.layoutManager = LinearLayoutManager(requireContext())
            listFile.adapter = fileAdapter
        }
    }

    private fun initData() {
        followList = mutableListOf()
        fileList = mutableListOf()
        viewModel.getFollowUser()
        viewModel.getFollowFile()
    }

}