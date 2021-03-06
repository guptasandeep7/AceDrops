package com.acedrops.acedrops.view.dash.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.acedrops.acedrops.R
import com.acedrops.acedrops.adapter.ReviewAdapter
import com.acedrops.acedrops.databinding.FragmentReviewBinding
import com.acedrops.acedrops.model.productDetails.ReviewsAndRating
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ReviewFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var reviewAdapter = ReviewAdapter()
    lateinit var name: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        getRatingAndReview()

        lifecycleScope.launch {
            name = Datastore(requireContext()).getUserDetails(Datastore.NAME_KEY).toString()
        }
        binding.postBtn.setOnClickListener(this)
    }

    private fun getRatingAndReview() {
        productViewModel.productDetails.observe(viewLifecycleOwner, {
            if (it is ApiResponse.Success) {
                binding.viewmodel = productViewModel
                binding.recyclerView.adapter = reviewAdapter
                if (it.data != null) {
                    reviewAdapter.updateReviewList(it.data.reviewsAndRatings)
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.post_btn -> if (!binding.review.text.isNullOrBlank()) postReviewAndRating()
            else binding.reviewLayout.helperText = "Please write a review"
        }
    }

    private fun postReviewAndRating() {
        val review = binding.review.text.toString().trim()
        val rating = binding.giveRating.rating.roundToInt().toString()
        productViewModel.postReviewAndRating(
            productViewModel.product.value!!.id,
            review,
            rating,
            requireContext()
        ).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    reviewAdapter.updateReviewList(
                        listOf(
                            ReviewsAndRating(
                                rating = rating.toInt(),
                                review = review,
                                userId = name
                            )
                        )
                    )
                    binding.recyclerView.adapter = reviewAdapter
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "You cannot rate this product",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
