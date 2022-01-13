package com.example.acedrops.adapter

import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.CartItemBinding
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.model.home.productId

class CartAdapter(
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var cartList = mutableListOf<Cart>()
    var favList = mutableListOf<productId>()
    fun updateProductList(product: List<Cart>, favProd: List<productId>) {
        this.cartList = product.toMutableList()
        this.favList = favProd.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun decreaseQuantity(position: Int)
        fun increaseQuantity(position: Int)
        fun addWishlist(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: CartItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Cart) {
            binding.product = product
            binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        init {
            itemView.setOnClickListener {
                listener.decreaseQuantity(adapterPosition)
                listener.increaseQuantity(adapterPosition)
                listener.addWishlist(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CartItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cart_item, parent, false
        )
        return ViewHolder(binding,mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartList[position])
        if(favList.contains(productId(cartList[position].id)))
            holder.binding.addToWishlistBtn.text  = "Remove From Wishlist"
        holder.binding.plusBtn.setOnClickListener {
            mlistner?.increaseQuantity(position)
            notifyDataSetChanged()
        }

        holder.binding.minusBtn.setOnClickListener {
            mlistner?.decreaseQuantity(position)
            notifyDataSetChanged()
        }

        holder.binding.addToWishlistBtn.setOnClickListener {
            mlistner?.addWishlist(position)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}