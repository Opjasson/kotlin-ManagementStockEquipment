package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.databinding.ViewholderItemcardBinding

class PopularAdapter :
    RecyclerView.Adapter<PopularAdapter.Viewholder>() {

    private val items = mutableListOf<ItemsModel>()

    inner class Viewholder(
        val binding: ViewholderItemcardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderItemcardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        Log.d("ITEM", item.nama)
        holder.binding.titleTxt.text = item.nama
    }

    override fun getItemCount(): Int = items.size

    // 🔥 INI YANG KAMU BUTUH
    fun setData(newItems: List<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}