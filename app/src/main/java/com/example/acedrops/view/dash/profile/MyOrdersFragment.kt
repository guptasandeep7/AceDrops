package com.example.acedrops.view.dash.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.MyOrdersAdapter
import com.example.acedrops.databinding.FragmentMyOrdersBinding
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.OrderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

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

            override fun onItemClick(position: Int) {

            }
        })

    }

    private fun getOrders() {
        orderViewModel.getOrders(requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    myOrdersAdapter.productList.clear()
                    it.data?.forEach {
                        myOrdersAdapter.updateOrderList(it.products)
                    }
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
            .setPositiveButton("Cancel") { dialog, id ->
                cancelOrder(position)
            }
            .setNeutralButton("Back") { dialog, id -> }
        val exit = builder.create()
        exit.show()
    }

    private fun cancelOrder(position: Int) {
        orderViewModel.cancelOrder(
            myOrdersAdapter.productList[position].order_item!!.orderId,
            requireContext()
        ).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    myOrdersAdapter.productList.removeAt(position)
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

    private fun snackbar(
        text: String
    ) {
        view?.let {
            Snackbar.make(
                it,
                text,
                Snackbar.LENGTH_SHORT
            ).setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
                .setAction("View Cart") {
                    findNavController()
                        .navigate(R.id.action_wishlistFragment_to_cartFragment)
                }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setAnchorView(R.id.bottomNavigationView)
                .show()
        }
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