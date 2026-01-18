package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.DetailActivity
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.databinding.ViewholderItemNotifikasiBinding
import com.example.nasibakarjoss18_application.databinding.ViewholderItemcardBinding


class AlatMakanAdapter :
    RecyclerView.Adapter<AlatMakanAdapter.Viewholder>() {

    private val items = mutableListOf<ItemsModel>()
    private lateinit var context: Context

    inner class Viewholder(
        val binding: ViewholderItemNotifikasiBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        context = parent.context
        val binding = ViewholderItemNotifikasiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]

        holder.binding.alatMakanCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("id", item.documentId)
                putExtra("nama", item.nama)
            }
            ContextCompat.startActivity(context, intent, null)
        }
        holder.binding.namaTxt.text = item.nama.replaceFirstChar { it.uppercase() }
        holder.binding.descTxt.text = item.deskripsi.replaceFirstChar { it.uppercase() }
        holder.binding.jumlahTxt.text = "Jumlah : " + item.jumlahBarang.toString()

    }

    override fun getItemCount(): Int = items.size

    fun setData(newItems: List<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    }