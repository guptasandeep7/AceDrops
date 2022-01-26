package com.example.acedrops.view.dash

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentProductBinding
import com.example.acedrops.model.home.ImgUrl
import com.example.acedrops.model.home.Product
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.ProductRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.example.acedrops.viewModelFactory.ProductViewModelFactory
import com.example.acedrops.viewmodel.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private lateinit var productViewModel: ProductViewModel
    private val binding get() = _binding!!
    private var product: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val view = binding.root

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        binding.product = product
        binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        product?.let { showImages(it.imgUrls) }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addToCartBtn.setOnClickListener {
            product?.id?.let { it1 -> productViewModel.addToCart(productId = it1) }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.atcResult.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> if (it.data == true) {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        view,
                        product?.title.toString(),
                        Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
                        .setAction("View Cart") {
                            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
                        }
                        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .setAnchorView(R.id.bottomNavigationView)
                        .show()
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()

                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun showImages(imgUrls: List<ImgUrl>) {
        val imageList = mutableListOf<SlideModel>()
        imgUrls.forEach {
            imageList.add(SlideModel(it.imageUrl, ScaleTypes.CENTER_CROP))
        }
        binding.productImages.setImageList(imageList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        product = arguments?.getSerializable("Product") as Product?

        productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ProductRepository(ServiceBuilder.buildService(ACC_TOKEN)))
        )[ProductViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }

}