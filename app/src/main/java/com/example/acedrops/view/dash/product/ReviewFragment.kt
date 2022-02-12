package com.example.acedrops.view.dash.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.adapter.ReviewAdapter
import com.example.acedrops.databinding.FragmentReviewBinding
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.ProductViewModel

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var reviewAdapter = ReviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        productViewModel.productDetails.observe(viewLifecycleOwner, {
            if(it is ApiResponse.Success){
                binding.viewmodel = productViewModel
                binding.recyclerView.adapter = reviewAdapter
                if(it.data!=null){
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
}
