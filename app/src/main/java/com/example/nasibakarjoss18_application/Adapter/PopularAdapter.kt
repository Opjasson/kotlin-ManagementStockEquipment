package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.databinding.ViewholderItemcardBinding

class PopularAdapter(val items : MutableList<ItemsModel>)
    : RecyclerView.Adapter<PopularAdapter.Viewholder>() {

        private lateinit var context: Context

    inner class Viewholder(val binding: ViewholderItemcardBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderItemcardBinding
            .inflate(LayoutInflater
                .from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: PopularAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt = item.nama
    }

    override fun getItemCount(): Int = items.size
}