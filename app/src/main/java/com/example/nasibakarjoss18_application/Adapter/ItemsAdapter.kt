package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.DetailActivity
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.databinding.ViewholderItemcardBinding

class ItemsAdapter(val items: MutableList<ItemsModel>):
    RecyclerView.Adapter<ItemsAdapter.Viewholder>(){

    private lateinit var context: Context

    inner class Viewholder( val binding: ViewholderItemcardBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapter.Viewholder {
        context= parent.context
        val binding = ViewholderItemcardBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ItemsAdapter.Viewholder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            titleTxt.text = item.nama.replaceFirstChar { it.uppercase() }
            jumlahtxtCard.text = item.jumlahBarang.toString() + "X"
            subtitleTxt.text = item.deskripsi.replaceFirstChar { it.uppercase() }
            Glide.with(context).load(item.imgUrl).into(pic)
        }

        holder.binding.popularCard.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("id", item.documentId)
                    putExtra("nama", item.nama)
                }
                ContextCompat.startActivity(context, intent, null)
            }, 500)

        }

    }
    override fun getItemCount(): Int = items.size

    fun updateData(newItems: MutableList<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}