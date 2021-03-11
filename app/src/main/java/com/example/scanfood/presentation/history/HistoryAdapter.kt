package com.example.scanfood.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanfood.databinding.ItemHistoryBinding
import com.example.scanfood.domain.Product
import com.example.scanfood.domain.toColorCategory
import com.example.scanfood.domain.toImage
import com.squareup.picasso.Picasso

class HistoryAdapter(private  var products: List<Product>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picasso = Picasso.get()
        val product = products.get(position)
        with(holder.binding){
            titleTextView.text = product.title
            expirationDateTextView.text = product.dateExp.toString()
            expirationDateTextView.setBackgroundColor(product.toColorCategory())
            picasso.load(product.image).into(itemImageView)
        }
    }

    override fun getItemCount(): Int = products.size
    fun updateDataSet(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }


}
