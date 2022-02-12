package com.example.acedrops.view.dash.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.MyOrdersAdapter
import com.example.acedrops.databinding.FragmentMyOrdersBinding
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.OrderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyOrdersFragment : Fragment() {
    private var _binding: FragmentMyOrdersBinding? = null
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val binding get() = _binding!!
    private val myOrdersAdapter = MyOrdersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE

        getOrders()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        myOrdersAdapter.setOnItemClickListener(object : MyOrdersAdapter.onItemClickListener {
            override fun cancelOrder(position: Int) {
                alertBox(position)
            }

            override fun onItemClick(product: Product) {
                val bundle = bundleOf("Product" to product)
                findNavController().navigate(
                    R.id.action_myOrdersFragment_to_productFragment,
                    bundle
                )
            }
        })

    }

    private fun getOrders() {
        orderViewModel.getOrders(requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { it1 -> myOrdersAdapter.updateOrderList(it1) }
                    binding.productsRecyclerView.adapter = myOrdersAdapter
                }

                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun alertBox(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm")
            .setMessage("Are you sure you want to cancel this order")
            .setPositiveButton("Cancel Order") { dialog, id ->
                cancelOrder(position)
            }
            .setNeutralButton("Back") { dialog, id -> }
        val exit = builder.create()
        exit.show()
    }

    private fun cancelOrder(position: Int) {
        orderViewModel.cancelOrder(
            myOrdersAdapter.orderList[position].id,
            requireContext()
        ).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    myOrdersAdapter.orderList.removeAt(position)
                    myOrdersAdapter.notifyDataSetChanged()
                }

                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }
}