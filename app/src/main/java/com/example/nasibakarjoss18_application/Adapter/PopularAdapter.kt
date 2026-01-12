package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.databinding.ViewholderItemcardBinding

class PopularAdapter :
    RecyclerView.Adapter<PopularAdapter.Viewholder>() {

    private val items = mutableListOf<ItemsModel>()
    private lateinit var context: Context

    inner class Viewholder(
        val binding: ViewholderItemcardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        context = parent.context
        val binding = ViewholderItemcardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text = item.nama

        Glide.with(context).load(item.imgUrl).into(holder.binding.pic)
    }

    override fun getItemCount(): Int = items.size

    // 🔥 INI YANG KAMU BUTUH
    fun setData(newItems: List<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}