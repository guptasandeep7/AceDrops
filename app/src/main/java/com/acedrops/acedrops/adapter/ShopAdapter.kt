package com.acedrops.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.ShopLayoutBinding
import com.acedrops.acedrops.model.home.Shop

class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    var shopsList = mutableListOf<Shop>()
    fun setShopList(shops: List<Shop>) {
        shopsList.clear()
        for (item in shops) {
            if (item.imgUrls.isNotEmpty())
                this.shopsList.add(item)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ShopLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: Shop) {
            binding.shop = shop
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ShopLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shop_layout, parent, false
        )
        return ViewHolder(binding, mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shopsList[position])
    }

    override fun getItemCount(): Int {
        return shopsList.size
    }

}