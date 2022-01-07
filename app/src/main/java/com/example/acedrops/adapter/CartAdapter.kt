package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.CartItemBinding
import com.example.acedrops.model.cart.Cart
import com.google.android.material.snackbar.Snackbar

class CartAdapter(
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var cartList = mutableListOf<Cart>()
    fun updateProductList(product: ArrayList<Cart>) {
        this.cartList = product.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Cart) {
            binding.product = product
            binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CartItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cart_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartList[position])
//        holder.binding.addToWishlistBtn.setOnClickListener {
//            this.cartList[position].also {
//                addToCart(it.id.toString(),it.title,holder)
//            }
//        }
    }

//    private fun addToCart(productId: String,title:String,holder: ViewHolder){
//        val repository = ProductsRepository(ServiceBuilder.buildService())
//
//        val result = repository.addToCart(productId = productId)
//        if(result) snackbar(title,holder)
//        else snackbar("Failed to add to cart",holder)
//    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun snackbar(
        text: String,
        holder: ViewHolder
    ) {
        Snackbar.make(
            holder.itemView,
            text,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            .setAction("View Cart") {
                holder.itemView.findNavController()
                    .navigate(R.id.action_homeFragment_to_cartFragment)
            }.setActionTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            .show()
    }

}