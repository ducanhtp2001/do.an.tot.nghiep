package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.GlobalFile
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserResponse

class GlobalFileRCAdapter(
    private val context: Context,
    private val user: User,
    private val list: MutableList<GlobalFile>,
    private val createContextMenu: (file: FileEntry) -> Unit,
    private val hideFile: (file: FileEntry, callback:() -> Unit) -> Unit,
    private val onOpenLikeDialog: (file: FileEntry) -> Unit,
    private val onOpenCommentDialog: (file: FileEntry) -> Unit,
    private val openFileDetail: (file: FileEntry, callback: (Evaluation) -> Unit) -> Unit,
    private val onLike: (file: FileEntry, callback: (Evaluation) -> Unit) -> Unit,
    private val onFollow: (file: FileEntry, callback: () -> Unit) -> Unit,
): RecyclerView.Adapter<GlobalFileRCAdapter.viewHolder>() {
    class viewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}