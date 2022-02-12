package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.CartItemBinding
import com.example.acedrops.databinding.MyorderItemBinding
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.model.home.Product

class MyOrdersAdapter : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    var productList = mutableListOf<Product>()
    fun updateOrderList(product: List<Product>) {
        this.productList = product.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun cancelOrder(position: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: MyorderItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        init {
            itemView.setOnClickListener {
                listener.cancelOrder(adapterPosition)
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MyorderItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.myorder_item, parent, false
        )
        return ViewHolder(binding, mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])

        holder.binding.cancelOrderBtn.setOnClickListener {
            mlistner?.cancelOrder(position)
        }

        holder.binding.productCard.setOnClickListener{
            mlistner?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}