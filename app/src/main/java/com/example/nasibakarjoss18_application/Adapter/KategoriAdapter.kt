package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasibakarjoss18_application.Domain.KategoriModel
import com.example.nasibakarjoss18_application.databinding.ViewholderKategoriBinding


class KategoriAdapter(val items : MutableList<KategoriModel>)
    : RecyclerView.Adapter<KategoriAdapter.Viewholder>() {

        private lateinit var context: Context

    inner class Viewholder(val binding: ViewholderKategoriBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderKategoriBinding
            .inflate(LayoutInflater
            .from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: KategoriAdapter.Viewholder, position: Int) {
        val item = items[position]
        Log.d("itemKatgeori", item.toString())
        holder.binding.titleCat.text = item.title
    }

    override fun getItemCount(): Int = items.size
}