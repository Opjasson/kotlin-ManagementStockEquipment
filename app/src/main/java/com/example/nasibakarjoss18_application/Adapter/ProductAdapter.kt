package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.EditProductActivity
import com.example.nasibakarjoss18_application.Activity.ManageProductActivity
import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.ViewModel.ProductViewModel
import com.example.nasibakarjoss18_application.databinding.ViewHolderCardProductBinding

class ProductAdapter(val items: MutableList<ProductModel>):
    RecyclerView.Adapter<ProductAdapter.Viewholder>() {
    private val viewModel = ProductViewModel()
    lateinit var context: Context

    class Viewholder(val binding: ViewHolderCardProductBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.Viewholder {
        context= parent.context
        val binding = ViewHolderCardProductBinding.
        inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.Viewholder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.nama_product
            .replaceFirstChar { it.uppercase() }
        holder.binding.priceTxt.text = "Rp " + item.harga_product.toString()
        holder.binding.subtitleTxt.text = item.deskripsi_product.take(50)
            .replaceFirstChar { it.uppercase() } + "..."

        Glide.with(context).load(item.imgUrl).into(holder.binding.pic)

        // Tombol Hapus
        holder.binding.deleteBtn.setOnClickListener {
            viewModel.deleteProduct(item.documentId)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ManageProductActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            }, 500)
        }

        // Klik Item untuk Ubah Data
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditProductActivity::class.java)
            intent.putExtra("object", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
