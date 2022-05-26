package com.acedrops.acedrops.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.ProductsLayoutBinding
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.model.home.ProductId

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

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(product: Product)
        fun onAddToCartClick(product: Product, view: View)
        fun onAddToWishlistClick(product: Product, view: View,position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: ProductsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            if(product.stock==0){
                binding.addToCartBtn.visibility = View.GONE
                binding.outOfStock.visibility = View.VISIBLE
            }
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
            mlistner?.onAddToCartClick(productList[position], it)
        }

        holder.binding.addToWishlistBtn.setOnClickListener {
            mlistner?.onAddToWishlistClick(productList[position], it, position)
        }

        holder.binding.productCard.setOnClickListener {
            mlistner?.onItemClick(productList[position])
        }

    }

//    private suspend fun addToWishlist(position: Int, holder: ViewHolder) {
//        val token = Datastore(holder.itemView.context).getUserDetails(ACCESS_TOKEN_KEY)
//
//        val call = ServiceBuilder.buildService(token)
//            .addToWishlist(productList[position].id.toString())
//        try {
//            call.enqueue(object : Callback<WishlistResponse?> {
//                override fun onResponse(
//                    call: Call<WishlistResponse?>,
//                    response: Response<WishlistResponse?>
//                ) {
//                    if (response.isSuccessful) {
//                        if (favList == null) {
//                            productList.removeAt(position)
//                            notifyDataSetChanged()
//                        } else {
//                            productList[position].wishlistStatus =
//                                response.body()?.status?.toInt()!!
//                            notifyItemChanged(position)
//                        }
//                    } else Toast.makeText(
//                        holder.itemView.context,
//                        "Failed to Add to wishlist",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onFailure(call: Call<WishlistResponse?>, t: Throwable) {
//                    Toast.makeText(
//                        holder.itemView.context,
//                        "Failed to Add to wishlist",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//        } catch (e: Exception) {
//            Toast.makeText(holder.itemView.context, "Failed to Add to wishlist", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }

    override fun getItemCount(): Int {
        return productList.size
    }

}