package com.example.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.databinding.CategoryLayoutBinding
import com.example.acedrops.model.CategoryList

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var categoryList = mutableListOf<CategoryList>()
    fun updateCategoryList(category: List<CategoryList>) {
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

    class ViewHolder(val binding: CategoryLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryList) {
            binding.category = category
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.category_layout, parent, false
        )
        return ViewHolder(binding, mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}