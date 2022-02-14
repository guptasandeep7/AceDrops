package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.MyorderItemBinding
import com.example.acedrops.model.home.Product

class OrderProductsAdapter : RecyclerView.Adapter<OrderProductsAdapter.ViewHolder>() {

    var productList = mutableListOf<Product>()
    fun updateOrderList(product: List<Product>) {
        this.productList = product.toMutableList()
        notifyDataSetChanged()
    }

    private lateinit var mlistner: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(product: Product)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: MyorderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MyorderItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.myorder_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])

        holder.binding.productCard.setOnClickListener {
            mlistner.onItemClick(productList[position])
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}