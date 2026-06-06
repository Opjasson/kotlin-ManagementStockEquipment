package com.example.nasibakarjoss18_application.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.NotaTransaksiActivity
import com.example.nasibakarjoss18_application.Domain.TransaksiWithCartModel
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.databinding.ViewHolderCardtransaksiBinding

class CardHistoryAdapter(private val items: MutableList<TransaksiWithCartModel>):
    RecyclerView.Adapter<CardHistoryAdapter.Viewholder>() {
    
    class Viewholder(val binding: ViewHolderCardtransaksiBinding):
        RecyclerView.ViewHolder(binding.root)
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewHolderCardtransaksiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        // Perbaikan: Cek apakah list cartItems tidak kosong sebelum akses index [0]
        if (item.cartItems.isNotEmpty()) {
            holder.binding.tvPelanggan.text = item.cartItems[0].username ?: "Pelanggan"
        } else {
            holder.binding.tvPelanggan.text = "Pelanggan"
        }

        holder.binding.tvIdPesanan.text = "No : ${item.transaksiId}"
        holder.binding.tvTotal.text = "Rp ${item.transaksi.totalHarga}"
        holder.binding.tvTanggal.text = item.transaksi.createdAt ?: "-"

        // Logika Menampilkan Bukti Transfer dengan Null Safety
        val buktiTf = item.transaksi.buktiTransfer
        if (!buktiTf.isNullOrEmpty()) {
            holder.binding.layoutBuktiTf.visibility = View.VISIBLE
            Glide.with(context)
                .load(buktiTf)
                .into(holder.binding.ivBuktiTf)
                
            holder.binding.ivBuktiTf.setOnClickListener {
                showImageDetail(context, buktiTf)
            }
        } else {
            holder.binding.layoutBuktiTf.visibility = View.GONE
        }

        // Setup Inner RecyclerView
        holder.binding.itemRv.apply {
            layoutManager = LinearLayoutManager(context)
            // Pastikan item.cartItems dikonversi ke MutableList jika dibutuhkan oleh adapter
            adapter = NamaItemAdapter(item.cartItems.toMutableList())
        }
        
        holder.binding.historyView.setOnClickListener {
            val intent = Intent(context, NotaTransaksiActivity::class.java)
            intent.putExtra("object", item)
            context.startActivity(intent)
        }
    }

    private fun showImageDetail(context: Context, imageUrl: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_detail)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        val imageView = dialog.findViewById<ImageView>(R.id.ivDetail)
        val closeBtn = dialog.findViewById<ImageView>(R.id.ivClose)
        
        Glide.with(context).load(imageUrl).into(imageView)
        
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun getItemCount(): Int = items.size
}
