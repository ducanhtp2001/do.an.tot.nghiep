package com.example.commyproject.activities.main.fragment.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.activities.main.fragment.home.global.PublicFragment
import com.example.commyproject.activities.main.fragment.home.local.LocalFragment
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.FragmentHomeBinding
import com.example.commyproject.ultil.adapter.ViewPagerAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.showSetConfigDialog
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var vpAdapter: ViewPagerAdapter
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        user = viewModel.getUser()

        initView()
        initEvent()
        initObserver()

//        Log.d("testing", "home create")

        return b.root
    }

    private fun initObserver() {
        viewModel.stateLoading.observe(requireActivity()) {
            if (!viewModel.stateLoading.value!!) {
            }
        }
    }

    private fun initEvent() {
        b.apply {
            fab.setOnClickListener {
                // open gallery, choose file and upload
                if (checkPermission()) {
                    requestPermission()
                } else readFile()
            }
        }
    }

    private fun initView() {
//        Log.d("testing", "initView")
        vpAdapter = ViewPagerAdapter(childFragmentManager)
        vpAdapter.add(LocalFragment(), "Private")
        vpAdapter.add(PublicFragment(), "Publish")

        b.viewPager.adapter = vpAdapter
        b.tabLayout.setupWithViewPager(b.viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_READ_STORAGE
        )
        if (checkPermission())
            startActivityForResult(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                    "package:${requireContext().packageName}"
                )
            ), PERMISSION_READ_STORAGE
        )
        else readFile()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSION_READ_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile()
            } else {
                startActivityForResult(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                            "package:${requireContext().packageName}"
                        )
                    ), 1001
                )
            }
        }
    }

    private fun readFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "application/pdf"

            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf", // PDF
//                "application/msword", // DOC
//                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOCX
//                "application/vnd.ms-excel", // EXCEL
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // XLSX
//                "image/*"
            ))
        }
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PDF && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                requireContext().showToast(uri.toString())
                requireContext().showSetConfigDialog {title, isTable, isPublic ->
                    val fileName = FileConverter.generateIdByUserId(user._id)
                    val description = "$title-$isTable-$isPublic"
                    viewModel.upload(requireContext(), uri, description, fileName, user._id) {
                        requireContext().showToast(viewModel.msg)
                    }
                }
            }
        }
    }
    companion object {
        const val PERMISSION_READ_STORAGE = 1000
        const val REQUEST_PDF = 100
    }
}