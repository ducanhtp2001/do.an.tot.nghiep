package com.example.commyproject.activities.bottomsheetdialog

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.databinding.DialogFileDetailBinding
import com.example.commyproject.ultil.getNavigationBarHeight
import com.example.commyproject.ultil.getStatusBarHeight
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.showFileDetailDialog(file: FileEntry) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogFileDetailBinding.inflate(layoutInflater, null, false)
    val windowHeight = requireActivity().window.decorView.height
    val statusBarHeight = getStatusBarHeight()
    val navigationBarHeight = getNavigationBarHeight()
    val usableHeight = windowHeight - statusBarHeight - navigationBarHeight
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        usableHeight
    )

    openFileDetailDialog(requireContext(), b, viewModel, file)
}

private fun openFileDetailDialog(
    context: Context?,
    b: DialogFileDetailBinding,
    viewModel: DialogViewModel,
    file: FileEntry
) {
    val bottomSheetDialog = BottomSheetDialog(context!!)

    bottomSheetDialog.setContentView(b.root)

    bottomSheetDialog.show()
}
