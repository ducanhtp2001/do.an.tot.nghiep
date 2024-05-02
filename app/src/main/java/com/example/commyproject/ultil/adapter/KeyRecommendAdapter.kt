package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.commyproject.R
import com.example.commyproject.ultil.Constant

class KeyRecommendAdapter(
    private val context: Context,
    private val searchByKey:(String) -> Unit
): RecyclerView.Adapter<KeyRecommendAdapter.viewHolder>() {

    var choose: Int = 0
    val chooseBackgroundColor = ContextCompat.getColor(context, R.color.black)
    val chooseTextColor = ContextCompat.getColor(context, R.color.white)
    class viewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtRecommend: TextView = view.findViewById(R.id.txtRecommend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_key_recommend, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return Constant.KEY_RECOMMEND_LIST.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.txtRecommend.text = Constant.KEY_RECOMMEND_LIST[position]

        if (position != choose) {
            holder.apply {
                holder.txtRecommend.setTextColor(chooseBackgroundColor)
                holder.txtRecommend.setBackgroundResource(R.drawable.bg_key_item)
            }
        } else {
            holder.apply {
                holder.txtRecommend.setTextColor(chooseTextColor)
                holder.txtRecommend.setBackgroundResource(R.drawable.bg_item_key_choosed)
            }
        }

        holder.txtRecommend.setOnClickListener {

            val old = choose
            choose = position
            holder.txtRecommend.setTextColor(chooseTextColor)
            holder.txtRecommend.setBackgroundResource(R.drawable.bg_item_key_choosed)
            notifyItemChanged(old)
            notifyItemChanged(choose)

            searchByKey(Constant.KEY_RECOMMEND_LIST[position])
        }
    }
}