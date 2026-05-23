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
        val item = items[position]

        holder.binding.tvPelanggan.text= item.cartItems[0].username
        holder.binding.tvIdPesanan.text= "No : " + item.transaksiId
        holder.binding.tvTotal.text= "Rp ${item.transaksi.totalHarga}"
        holder.binding.tvTanggal.text= item.transaksi.createdAt

        // Logika Menampilkan Bukti Transfer
        if (!item.transaksi.buktiTransfer.isNullOrEmpty()) {
            holder.binding.layoutBuktiTf.visibility = View.VISIBLE
            Glide.with(context)
                .load(item.transaksi.buktiTransfer)
                .into(holder.binding.ivBuktiTf)
                
            // Klik gambar untuk memperbesar
            holder.binding.ivBuktiTf.setOnClickListener {
                showImageDetail(item.transaksi.buktiTransfer)
            }
        } else {
            holder.binding.layoutBuktiTf.visibility = View.GONE
        }

        holder.binding.itemRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NamaItemAdapter(item.cartItems.toMutableList())
        }
        
        holder.binding.historyView.setOnClickListener {
            val intent = Intent(context, NotaTransaksiActivity::class.java)
            intent.putExtra("object", item)
            context.startActivity(intent)
        }
    }

    private fun showImageDetail(imageUrl: String) {
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

    override fun getItemCount(): Int =items.size
}