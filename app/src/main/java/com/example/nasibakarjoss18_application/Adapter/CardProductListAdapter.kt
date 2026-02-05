package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.databinding.ViewHolderCartListBinding

class CardProductListAdapter(
    private val onAddToCart : (String) -> Unit
    ,val items: MutableList<ProductModel>
):
    RecyclerView.Adapter<CardProductListAdapter.Viewholder>() {
    lateinit var context: Context

    class Viewholder(val binding: ViewHolderCartListBinding):
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardProductListAdapter.Viewholder {
        context= parent.context
        val binding = ViewHolderCartListBinding.
        inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CardProductListAdapter.Viewholder, position: Int) {
        holder.binding.titleTxt.text= items[position].nama_product
            .replaceFirstChar { it.uppercase() }
        holder.binding.priceTxt.text="$"+items[position].harga_product.toString()
        holder.binding.subtitleTxt.text= items[position].deskripsi_product.toString().take(50)
            .replaceFirstChar { it.uppercase() } + "..."

        Glide.with(context).load(items[position].imgUrl).into(holder.binding.pic)

        holder.binding.view.setOnClickListener {
            onAddToCart(items[position].documentId)

        }

    }

    override fun getItemCount(): Int =items.size

    fun updateData(newItems: MutableList<ProductModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}