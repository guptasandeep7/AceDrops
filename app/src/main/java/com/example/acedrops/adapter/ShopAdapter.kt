package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.ShopLayoutBinding
import com.example.acedrops.model.home.Shop

class ShopAdapter(
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    var shopsList = mutableListOf<Shop>()
    fun setShopList(shops: List<Shop>) {
        this.shopsList = shops.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ShopLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(shop:Shop){
            binding.shop = shop
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:ShopLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.shop_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shopsList[position])
    }

    override fun getItemCount(): Int {
        return shopsList.size
    }

}