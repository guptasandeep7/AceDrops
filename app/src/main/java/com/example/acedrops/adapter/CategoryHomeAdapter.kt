package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.OneCategoryLayoutBinding
import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.ProductId

class CategoryHomeAdapter : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {

    var categoryList = mutableListOf<Category>()
    var favList = mutableListOf<ProductId>()
    fun updateCategoryList(category: List<Category>, favList: List<ProductId>) {
        category.forEach {
            if(!it.products.isNullOrEmpty())
                categoryList.add(it)
        }
        this.favList = favList.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun showAll(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: OneCategoryLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        private val productAdapter = ProductAdapter()

        fun bind(category: Category, favList: List<ProductId>) {
            binding.category = category
            binding.productsRecyclerView.adapter = productAdapter
            productAdapter.updateProductList(category.products, favList)
        }

        init {
            itemView.setOnClickListener {
                listener.showAll(adapterPosition)
            }
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