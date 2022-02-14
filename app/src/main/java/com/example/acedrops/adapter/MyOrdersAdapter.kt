package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.OrderProductItemBinding
import com.example.acedrops.model.MyOrders
import com.example.acedrops.model.home.Product

class MyOrdersAdapter : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    var orderList = mutableListOf<MyOrders>()
    fun updateOrderList(product: List<MyOrders>) {
        this.orderList = product.toMutableList()
        notifyDataSetChanged()
    }

    private lateinit var mlistner: onItemClickListener

    interface onItemClickListener {
        fun cancelOrder(position: Int)
        fun onItemClick(product: Product)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: OrderProductItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        private val orderProductAdapter = OrderProductsAdapter()
        fun bind(myOrder: MyOrders) {
            binding.order = myOrder
            orderProductAdapter.updateOrderList(myOrder.products)
            binding.productsRecyclerView.adapter = orderProductAdapter
        }

        init {
            orderProductAdapter.setOnItemClickListener(object :
                OrderProductsAdapter.onItemClickListener {
                override fun onItemClick(product: Product) {
                    listener.onItemClick(product)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OrderProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.order_product_item, parent, false
        )
        return ViewHolder(binding, mlistner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position])

        holder.binding.cancelBtn.setOnClickListener {
            mlistner.cancelOrder(position)
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}