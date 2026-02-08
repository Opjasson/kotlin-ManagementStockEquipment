package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasibakarjoss18_application.Domain.HistoryProductModel
import com.example.nasibakarjoss18_application.databinding.ViewHolderItemHistoryBinding

class NamaItemAdapter(val items: MutableList<HistoryProductModel>):
    RecyclerView.Adapter<NamaItemAdapter.Viewholder>() {

    lateinit var context: Context
    class Viewholder(val binding: ViewHolderItemHistoryBinding):
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaItemAdapter.Viewholder {
        context= parent.context
        val binding = ViewHolderItemHistoryBinding.
        inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: NamaItemAdapter.Viewholder, position: Int) {
        Log.d("DATAHOSTORY", items[position].nama.toString())
        holder.binding.itemName.text = items[position].nama
    }
    override fun getItemCount(): Int =items.size
}