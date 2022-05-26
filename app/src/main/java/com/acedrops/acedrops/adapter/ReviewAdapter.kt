package com.acedrops.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.ReviewItemBinding
import com.acedrops.acedrops.model.productDetails.ReviewsAndRating

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    var reviewList = mutableListOf<ReviewsAndRating>()
    fun updateReviewList(review: List<ReviewsAndRating>) {
        this.reviewList = review.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewsAndRating) {
            binding.review = review
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ReviewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.review_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

}