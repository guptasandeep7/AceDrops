package com.acedrops.acedrops.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.OneCategoryLayoutBinding
import com.acedrops.acedrops.model.home.Category
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.model.home.ProductId

class CategoryHomeAdapter :
    RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {

    var categoryList = mutableListOf<Category>()
    var favList = mutableListOf<ProductId>()
    fun updateCategoryList(category: List<Category>, favList: List<ProductId>) {
        this.favList.clear()
        categoryList.clear()
        category.forEach {
            if (!it.products.isNullOrEmpty())
                categoryList.add(it)
        }
        this.favList = favList.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun showAll(position: Int)
        fun addToCartClick(product:Product,view: View)
        fun addToWishlistClick(product:Product,view: View)
        fun onItemClick(product:Product)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: OneCategoryLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        var productAdapter = ProductAdapter()

        fun bind(category: Category, favList: List<ProductId>) {
            binding.category = category
            binding.productsRecyclerView.adapter = productAdapter
            productAdapter.updateProductList(category.products, favList)
        }
        init {
            productAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
                override fun onItemClick(product: Product) {
                    listener.onItemClick(product = product)
                }

                override fun onAddToCartClick(product: Product, view: View) {
                    listener.addToCartClick(product,view)
                }

                override fun onAddToWishlistClick(product: Product, view: View, position: Int) {
                    listener.addToWishlistClick(product,view)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OneCategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.one_category_layout, parent, false
        )
        return ViewHolder(binding, mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], favList)

        holder.binding.showAllBtn.setOnClickListener {
            mlistner?.showAll(position)
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}