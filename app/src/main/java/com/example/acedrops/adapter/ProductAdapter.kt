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
import com.example.acedrops.model.Message
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.home.Product
import com.example.acedrops.model.home.productId
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var productList = mutableListOf<Product>()
    var favList = mutableListOf<productId>()
    fun updateProductList(product: List<Product>,favList: List<productId>) {
        this.productList = product.toMutableList()
        this.favList = favList.toMutableList()
        for (item in productList) if (favList.contains(productId(item.id))) item.wishlistStatus = 1
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
        holder.binding.addToCartBtn.setOnClickListener { addToCart(position, holder) }
//        holder.binding.addToWishlistBtn.setOnClickListener { addToWishlist(position, holder) }
    }


    private fun addToCart(
        position: Int,
        holder: ViewHolder
    ) {
        this.productList[position].also {
            ServiceBuilder.buildService(token = ACC_TOKEN)
                .addToCart(it.id.toString())
                .enqueue(object : Callback<CartResponse?> {
                    override fun onResponse(
                        call: Call<CartResponse?>,
                        response: Response<CartResponse?>
                    ) {
                        if (response.isSuccessful) {
                            snackbar(
                                it.title,
                                holder
                            )
                        } else snackbar(
                            response.message() ?: "Failed to add to cart ${response.code()}", holder
                        )
                    }

                    override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                        snackbar("Failed to add to cart", holder)
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return productList.size
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
            .setAnchorView(R.id.bottomNavigationView)
            .show()
    }

}