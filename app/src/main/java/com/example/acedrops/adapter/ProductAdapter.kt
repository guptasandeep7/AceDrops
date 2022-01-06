package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.ProductsLayoutBinding
import com.example.acedrops.model.home.Product
import com.google.android.material.snackbar.Snackbar

class ProductAdapter(
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var productList = mutableListOf<Product>()
    fun updateProductList(product: List<Product>) {
        this.productList = product.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ProductsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.products_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
        holder.binding.addToCartBtn.setOnClickListener {
            snackbar(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private fun snackbar(
        holder: ViewHolder,
        position: Int
    ) {
        Snackbar.make(
            holder.itemView,
            "${this.productList[position].title}\nRs${this.productList[position].basePrice}",
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            .setAction("View Cart") {
                holder.itemView.findNavController()
                    .navigate(R.id.action_homeFragment_to_cartFragment)
            }.setActionTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            .show()
    }

}