package com.example.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.ProductsLayoutBinding
import com.example.acedrops.model.cart.CartResponse
import com.example.acedrops.model.cart.WishlistResponse
import com.example.acedrops.model.home.Product
import com.example.acedrops.model.home.ProductId
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var productList = mutableListOf<Product>()
    var favList: MutableList<ProductId>? = null
    fun updateProductList(product: List<Product>, favList: List<ProductId>?) {
        this.productList = product.toMutableList()
        if (favList != null) {
            this.favList = favList.toMutableList()
            for (item in productList) if (favList.contains(ProductId(item.id))) item.wishlistStatus =
                1
        }
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
            it.isEnabled = false
            addToCart(position, holder)
        }
        holder.binding.addToWishlistBtn.setOnClickListener {
            addToWishlist(position, holder)
        }
        holder.binding.productCard.setOnClickListener {
            val bundle = bundleOf("Product" to productList[position])
            holder.itemView.findNavController().navigate(R.id.action_homeFragment_to_productFragment,bundle)
        }
    }

    private fun addToWishlist(position: Int, holder: ProductAdapter.ViewHolder) {
        val call = ServiceBuilder.buildService(ACC_TOKEN)
            .addToWishlist(productList[position].id.toString())
        try {
            call.enqueue(object : Callback<WishlistResponse?> {
                override fun onResponse(
                    call: Call<WishlistResponse?>,
                    response: Response<WishlistResponse?>
                ) {
                    if (response.isSuccessful) {
                        if (favList == null) {
                            productList.removeAt(position)
                            notifyDataSetChanged()
                        } else {
                            productList[position].wishlistStatus =
                                response.body()?.status?.toInt()!!
                            notifyItemChanged(position)
                        }
                    } else Toast.makeText(
                        holder.itemView.context,
                        "Failed to Add to wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<WishlistResponse?>, t: Throwable) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed to Add to wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(holder.itemView.context, "Failed to Add to wishlist", Toast.LENGTH_SHORT)
                .show()
        }
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
                            holder.binding.addToCartBtn.isEnabled = false
                            snackbar(
                                "\u20B9${it.discountedPrice} plus taxes\n1 ITEM",
                                holder
                            )
                        } else {
                            holder.binding.addToCartBtn.isEnabled = true
                            snackbar(
                                response.message() ?: "Failed to add to cart : Try Again",
                                holder
                            )
                        }
                    }

                    override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                        holder.binding.addToCartBtn.isEnabled = true
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
            holder.itemView.rootView,
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