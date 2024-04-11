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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.activities.main.fragment.home.global.PublicFragment
import com.example.commyproject.activities.main.fragment.home.local.LocalFragment
import com.example.commyproject.databinding.FragmentHomeBinding
import com.example.commyproject.ultil.adapter.ViewPagerAdapter
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var vpAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        initView()
        initEvent()
        initObserver()

        return b.root
    }

    private fun initObserver() {
        viewModel.stateLoading.observe(requireActivity(), Observer {
            if (!viewModel.stateLoading.value!!) {
                requireContext().showToast(requireContext(), viewModel.msg)
            }
        })
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
        vpAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        vpAdapter.add(LocalFragment(), "Private")
        vpAdapter.add(PublicFragment(), "Publish")

        b.viewPager.adapter = vpAdapter
        b.tabLayout.setupWithViewPager(b.viewPager)
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

            type = "*/*" // Tất cả các loại tệp
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf", // PDF
                "application/msword", // DOC
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOCX
                "application/vnd.ms-excel", // EXCEL
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // XLSX
                "image/*" // Hình ảnh (tất cả các định dạng hình ảnh)
            ))
        }
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PDF && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                requireContext().showToast(requireContext(), uri.toString())
                viewModel.upload(requireContext(), uri, "abc", Calendar.getInstance().timeInMillis.toString())
            }
        }
    }
    companion object {
        const val PERMISSION_READ_STORAGE = 1000
        const val REQUEST_PDF = 100
    }
}