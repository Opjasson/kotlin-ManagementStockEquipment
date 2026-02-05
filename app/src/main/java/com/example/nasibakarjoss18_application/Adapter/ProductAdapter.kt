package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Activity.CashierActivity
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
        holder.binding.titleTxt.text= items[position].nama_product
            .replaceFirstChar { it.uppercase() }
        holder.binding.priceTxt.text="$"+items[position].harga_product.toString()
        holder.binding.subtitleTxt.text= items[position].deskripsi_product.toString().take(50)
            .replaceFirstChar { it.uppercase() } + "..."

        Glide.with(context).load(items[position].imgUrl).into(holder.binding.pic)

        holder.binding.deleteBtn.setOnClickListener {
            viewModel.deleteProduct(items[position].documentId)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, CashierActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            }, 500)
        }


        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("object", items[position])
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =items.size
}