package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.NotaTransaksiActivity
import com.example.nasibakarjoss18_application.Domain.TransaksiWithCartModel
import com.example.nasibakarjoss18_application.databinding.ViewHolderCardtransaksiBinding

class CardHistoryAdapter(val items: MutableList<TransaksiWithCartModel>):
    RecyclerView.Adapter<CardHistoryAdapter.Viewholder>() {
    lateinit var context: Context
    class Viewholder(val binding: ViewHolderCardtransaksiBinding):
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHistoryAdapter.Viewholder {
        context= parent.context
        val binding = ViewHolderCardtransaksiBinding.
        inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CardHistoryAdapter.Viewholder, position: Int) {

        holder.binding.tvPelanggan.text= items[position].cartItems[0].username
        holder.binding.tvIdPesanan.text= "No : " + items[position].transaksiId
        holder.binding.tvTotal.text= items[position].transaksi.totalHarga.toString()
        holder.binding.tvTanggal.text= items[position].transaksi.createdAt


        holder.binding.itemRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NamaItemAdapter(items[position].cartItems.toMutableList())
        }
        holder.binding.historyView.setOnClickListener {
            val intent = Intent(context, NotaTransaksiActivity::class.java)
            intent.putExtra("object", items[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =items.size
}