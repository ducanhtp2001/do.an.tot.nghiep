package com.example.commyproject.activities.main.fragment.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.activities.bottomsheetdialog.runOnUiThread
import com.example.commyproject.activities.bottomsheetdialog.showFileDetailDialog
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.Notification
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.databinding.FragmentNotificationBinding
import com.example.commyproject.ultil.adapter.NotificationRCAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private lateinit var b: FragmentNotificationBinding
    private lateinit var viewModel: NotificationFragmentViewModel
    private lateinit var listNotification: MutableList<Notification>
    private lateinit var adapter: NotificationRCAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentNotificationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NotificationFragmentViewModel::class.java]

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
            list.observe(requireActivity()) {
                it?.let { 
                    if (it.isNotEmpty()) {
                        listNotification.clear()
                        listNotification.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            file.observe(requireActivity()) {

            }
        }
    }

    private fun initView() {
        adapter = NotificationRCAdapter(
            requireContext(),
            listNotification,
            onClick = {
                val file = FileEntity(it)
                viewModel.getSingleFile(file) {
                    runOnUiThread {
                        it?.let {
                            requireActivity().showFileDetailDialog(it,
                                updateState = { _, _ ->

                                },
                                updateLike = { _ ->

                                },
                                onDelete = {})
                        }
                    }
                }
            },
            openMenu = {

            }
        )
        b.rcvNotification.layoutManager = LinearLayoutManager(requireContext())
        b.rcvNotification.adapter = adapter
    }

    private fun initData() {
        listNotification = mutableListOf()
        val userId = UserEntity(viewModel.user._id)
        viewModel.getNotifications(userId)
    }

}