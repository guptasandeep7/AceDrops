package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.CartItemBinding
import com.example.acedrops.model.cart.Cart

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
            if (product.cart_item.quantity == 1) {
                binding.minusBtn.setBackgroundResource(R.drawable.ic_delete)
            }
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

        holder.binding.plusBtn.setOnClickListener {
            cartList[position].cart_item.quantity++
            notifyItemChanged(position)
        }

        holder.binding.minusBtn.setOnClickListener {
            if(cartList[position].cart_item.quantity>1){
                cartList[position].cart_item.quantity--
                notifyItemChanged(position)
            }
            else{
                cartList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,cartList.size)
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}