package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Notification
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter

class NotificationRCAdapter(
    private val context: Context,
    private val list: MutableList<Notification>,
    private val onClick: (idFile: String) -> Unit,
    private val openMenu: (idFile: String) -> Unit
) : RecyclerView.Adapter<NotificationRCAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val content: TextView = view.findViewById(R.id.content)
        val time: TextView = view.findViewById(R.id.time)
        val menu: ImageView = view.findViewById(R.id.menu)
        val item: View = view.findViewById(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            val avatarUrl = Config.SERVER_URL + data.avatar
            Glide.with(context)
                .load(avatarUrl)
                .into(holder.avatar)

            content.text = data.content
            val timeString = FileConverter.getTimePassFromString(data.time)
            time.text = timeString
            item.setOnClickListener {
                onClick(data.idFile)
            }
            menu.setOnClickListener {
                openMenu(data.idFile)
            }
        }
    }
}