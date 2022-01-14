package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.OneCategoryLayoutBinding
import com.example.acedrops.model.home.Category

class CategoryHomeAdapter : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {

    var categoryList = mutableListOf<Category>()
    fun updateCategoryList(category: List<Category>) {
        this.categoryList = category.toMutableList()
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
        fun bind(category: Category) {
            binding.category = category
            val productAdapter = ProductAdapter()
            binding.productsRecyclerView.adapter = productAdapter
            productAdapter.updateProductList(category.products)
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
        holder.bind(categoryList[position])
        holder.binding.showAllBtn.setOnClickListener {
            val bundle = bundleOf("ProductList" to categoryList[position].products)
            holder.itemView.findNavController()
                .navigate(R.id.action_homeFragment_to_allProductsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}