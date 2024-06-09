package com.example.commyproject.activities.main.fragment.global

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.activities.bottomsheetdialog.postLike
import com.example.commyproject.activities.bottomsheetdialog.showCommentDialog
import com.example.commyproject.activities.bottomsheetdialog.showContextMenuDialog
import com.example.commyproject.activities.bottomsheetdialog.showFileDetailDialog
import com.example.commyproject.activities.bottomsheetdialog.showLikeDialog
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.KeyRecommend
import com.example.commyproject.data.model.requestmodel.RequestFollow
import com.example.commyproject.databinding.FragmentGlobalBinding
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.adapter.GlobalFileRCAdapter
import com.example.commyproject.ultil.adapter.KeyRecommendAdapter
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalFragment : Fragment() {
    private lateinit var viewModel: GlobalFragmentViewModel
    private lateinit var b: FragmentGlobalBinding
    private lateinit var listFile: MutableList<FileEntry>
    private lateinit var keyAdapter: KeyRecommendAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var fileAdapter: GlobalFileRCAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentGlobalBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GlobalFragmentViewModel::class.java]

        initData()
        initView()
        initEvent()
        initObserver()

        val keyRecommend = KeyRecommend(null, viewModel.time, viewModel.spinnerOption, listFile.map { it._id })
        viewModel.getGlobalFile(keyRecommend)

        return b.root
    }

    private fun initEvent() {
        b.apply {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.spinnerOption = position
//                    requireContext().showToast("position = ${viewModel.spinnerOption}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.spinnerOption = 0
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        keyAdapter = KeyRecommendAdapter(requireContext()) { keyword ->
            val keyRecommend = KeyRecommend(keyword, viewModel.time, viewModel.spinnerOption, listFile.map { it._id })
            viewModel.getGlobalFile(keyRecommend)
        }
        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Constant.SPINNER_OPTION)

        b.apply {
            inputKeyword.imeOptions = EditorInfo.IME_ACTION_DONE
            inputKeyword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    find()
                    return@setOnEditorActionListener true
                } else {
                    return@setOnEditorActionListener false
                }
            }
            btnFind.setOnClickListener { find() }

            listKeyRecommend.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            listKeyRecommend.adapter = keyAdapter
            spinner.adapter = spinnerAdapter
            
            fileAdapter = GlobalFileRCAdapter(
                requireActivity(), 
                viewModel.user,
                this@GlobalFragment.listFile,
                createContextMenu = { file ->
                    requireActivity().showContextMenuDialog(
                        file,
                        updateState = { response, mFile ->
                            requireActivity().showToast(response.msg)
                            this@GlobalFragment.listFile.removeIf {
                                it._id == mFile._id
                            }
                            requireActivity().runOnUiThread {
                                fileAdapter.notifyDataSetChanged()
                            }
                        },
                        onDelete = { fileId ->
                            requireActivity().runOnUiThread {
                                this@GlobalFragment.listFile.removeIf { it._id == fileId}
                                this@GlobalFragment.fileAdapter.notifyDataSetChanged()
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
                            this@GlobalFragment.listFile.removeIf { it._id == mFile._id }
                            requireActivity().runOnUiThread {
                                this@GlobalFragment.fileAdapter.notifyDataSetChanged()
                            }
                        },
                        updateLike = { evaluation ->
                            updateLike(evaluation)
                            val position = this@GlobalFragment.listFile.indexOf(file)
                            if (this@GlobalFragment.listFile[position].likes.any { it.idUser == evaluation.idUser }) {
                                this@GlobalFragment.listFile[position].likes.removeIf{ it.idUser == evaluation.idUser }
                            } else {
                                this@GlobalFragment.listFile[position].likes.add(evaluation)
                            }
                        },
                        onDelete = { fileId ->
                            requireActivity().runOnUiThread {
                                this@GlobalFragment.listFile.removeIf { it._id == fileId}
                                this@GlobalFragment.fileAdapter.notifyDataSetChanged()
                            }
                        }
                    )
                },
                onLike = { file, callback ->
                    postLike(file, null, EvaluationEntityType.FILE, viewModel.user._id, viewModel) { evaluation ->
                        callback(evaluation)
                    }
                },
                onFollow = { file, callback ->
                    val data = RequestFollow(viewModel.user._id, file.idUser)
                    viewModel.followUser(data) {
                        requireActivity().runOnUiThread {
                            requireContext().showToast(it)
                            callback()
                        }
                    }
                })
            listFile.layoutManager = LinearLayoutManager(requireContext())
            listFile.adapter = fileAdapter
        }
    }

    private fun find() {
        val keyword = b.inputKeyword.text.toString()
        if (keyword.isNotEmpty()) {
            val keyRecommend = KeyRecommend(keyword, viewModel.time, viewModel.spinnerOption, listFile.map { it._id })
            viewModel.getGlobalFile(keyRecommend)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.list.observe(requireActivity()) {
            if (it != null) {
                listFile.clear()
                listFile.addAll(it)
                fileAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initData() {
        listFile = mutableListOf()
    }
}