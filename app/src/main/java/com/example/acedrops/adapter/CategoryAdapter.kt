package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.OneCategoryLayoutBinding
import com.example.acedrops.model.home.Category

class CategoryAdapter(
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList = mutableListOf<Category>()
    fun updateCategoryList(category: List<Category>) {
        this.categoryList = category.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: OneCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            val productAdapter = ProductAdapter()
            binding.productsRecyclerView.adapter = productAdapter
            productAdapter.updateProductList(category.products)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OneCategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.one_category_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}