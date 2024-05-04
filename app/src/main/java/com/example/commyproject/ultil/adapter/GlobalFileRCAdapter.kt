package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter

class GlobalFileRCAdapter(
    private val context: Context,
    private val user: User, // la account cua nguoi dung
    private val list: MutableList<FileEntry>,
    private val createContextMenu: (file: FileEntry) -> Unit,
    private val hideFile: (file: FileEntry, callback:() -> Unit) -> Unit,
    private val onOpenLikeDialog: (file: FileEntry) -> Unit,
    private val onOpenCommentDialog: (file: FileEntry) -> Unit,
    private val openFileDetail: (file: FileEntry, callback: (Evaluation) -> Unit) -> Unit,
    private val onLike: (file: FileEntry, callback: (Evaluation) -> Unit) -> Unit,
    private val onFollow: (file: FileEntry, callback: () -> Unit) -> Unit,
): RecyclerView.Adapter<GlobalFileRCAdapter.viewHolder>() {

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val likeColor = ContextCompat.getColor(context, R.color.like)
        val nonLikeColor = ContextCompat.getColor(context, R.color.black)

        val data = list[position]
        holder.txtUserName.text = data.userName
        holder.txtTitle.text = data.title
        holder.txtTime.text = FileConverter.getTimePassFromId(data._id)

        val avatarUrl = Config.SERVER_URL + data.avatar
        Glide.with(context)
            .load(avatarUrl)
            .into(holder.avatar)

        holder.apply {
            if (list[position].likes.any{it.idUser == user._id}) {
                btnLikeTxt.setTextColor(likeColor)
            } else {
                btnLikeTxt.setTextColor(nonLikeColor)
            }
        }

        holder.apply {
            txtContent.text = data.summaryText

            if (txtContent.maxLines < Int.MAX_VALUE) {
                btnMoreContent.visibility = View.GONE
            }
            btnMoreContent.setOnClickListener {
                txtContent.maxLines = Int.MAX_VALUE
                it.visibility = View.GONE
            }
        }

        holder.apply {
            btnLike.setOnClickListener {
                onLike(data) { evaluation ->
                    if (list[position].likes.any { it.idUser == evaluation.idUser } ) {
                        list[position].likes.removeIf { it.idUser == evaluation.idUser }
                        btnLikeTxt.setTextColor(nonLikeColor)
                    } else {
                        list[position].likes.add(evaluation)
                        btnLikeTxt.setTextColor(likeColor)
                    }
                    btnLike.post {
                        notifyDataSetChanged()
                    }
                }
            }
            btnHide.setOnClickListener {
                hideFile(data) {
                    list.remove(data)
                    btnHide.post {
                        notifyDataSetChanged()
                    }
                }
            }
            if (user._id == data.idUser) {
                btnFollow.apply {
                    isEnabled = false
                    visibility = View.GONE
                }
            }
            btnFollow.setOnClickListener {
                onFollow(data) {
                    // =============================================================================
                }
            }
            info.setOnClickListener {
                openFileDetail(data) { evaluation ->
                    if (list[position].likes.any{ it.idUser == evaluation.idUser }) {
                        list[position].likes.removeIf { it.idUser == evaluation.idUser }
                    } else {
                        list[position].likes.add(evaluation)
                    }

                    btnMenu.post {
                        notifyDataSetChanged()
                    }
                }
            }
            txtContent.setOnClickListener {
                openFileDetail(data) {evaluation ->
                    if (list[position].likes.any{ it.idUser == evaluation.idUser }) {
                        list[position].likes.removeIf { it.idUser == evaluation.idUser }
                    } else {
                        list[position].likes.add(evaluation)
                    }

                    btnMenu.post {
                        notifyDataSetChanged()
                    }
                }
            }

            likeCount.text = data.likes?.size.toString() ?: "0"
//            context.getString(R.string.upvote, data.likes?.size ?: 0)
            btnOpenLike.setOnClickListener {
                onOpenLikeDialog(data)
            }

            commentCount.text = data.comments?.size.toString() ?: "0"
//                context.getString(R.string.comments, data.comments?.size ?: 0)
            btnOpenComment.setOnClickListener {
                onOpenCommentDialog(data)
            }
            btnComment.setOnClickListener {
                onOpenCommentDialog(data)
            }
            btnMenu.setOnClickListener {
                createContextMenu(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_global_file, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtUserName: TextView = view.findViewById(R.id.txtUserName)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.content)
        val btnMoreContent: TextView = view.findViewById(R.id.btnMoreContent)
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val btnMenu: ImageView = view.findViewById(R.id.btnMenu)
        val btnHide: ImageView = view.findViewById(R.id.btnHide)
        val likeCount: TextView = view.findViewById(R.id.likeCount)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val info : View = view.findViewById(R.id.info)
        val btnLike : View = view.findViewById(R.id.btnLike)
        val btnLikeTxt : TextView = view.findViewById(R.id.btnLikeTxt)
        val btnFollow : TextView = view.findViewById(R.id.btnFollow)
        val btnOpenLike : View = view.findViewById(R.id.btnOpenLike)
        val btnComment : View = view.findViewById(R.id.btnComment)
        val btnOpenComment : View = view.findViewById(R.id.btnOpenComment)
    }
}