package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.OneCategoryLayoutBinding
import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.ProductId

class CategoryHomeAdapter() : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {

    var categoryList = mutableListOf<Category>()
    var favList = mutableListOf<ProductId>()
    fun updateCategoryList(category: List<Category>,favList: List<ProductId>) {
        this.categoryList = category.toMutableList()
        this.favList = favList.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: OneCategoryLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, favList: List<ProductId>) {
            binding.category = category
            val productAdapter = ProductAdapter()
            binding.productsRecyclerView.adapter = productAdapter
            productAdapter.updateProductList(category.products,favList)
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
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
        holder.bind(categoryList[position],favList)
        holder.binding.showAllBtn.setOnClickListener {
            mlistner?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}