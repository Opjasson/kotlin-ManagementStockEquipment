package com.example.nasibakarjoss18_application.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasibakarjoss18_application.Domain.LaporanProductModel
import com.example.nasibakarjoss18_application.databinding.ViewholderLaporanItemBinding
import java.text.NumberFormat
import java.util.Locale

class LaporanAdapter(private var items: List<LaporanProductModel>) :
    RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

    private val formatter = NumberFormat.getInstance(Locale("id", "ID"))

    class ViewHolder(val binding: ViewholderLaporanItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderLaporanItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvItemName.text = item.nama
        holder.binding.tvItemQty.text = "${item.jumlah}x"
        val subtotal = item.harga * item.jumlah
        holder.binding.tvSubtotal.text = "Rp ${formatter.format(subtotal)}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<LaporanProductModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}