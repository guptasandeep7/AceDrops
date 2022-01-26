package com.example.acedrops.view.dash

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.SearchAdapter
import com.example.acedrops.databinding.FragmentSearchBinding
import com.example.acedrops.model.search.SearchItem
import com.example.acedrops.model.search.SearchResult
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.SearchRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.example.acedrops.viewModelFactory.SearchViewModelFactory
import com.example.acedrops.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    private val searchAdapter = SearchAdapter()
    private var searchList = mutableListOf<SearchItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!s.isNullOrBlank()) searchViewModel.getSearch(s.toString())
                observerSearch()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        searchAdapter.setOnItemClickListener(object : SearchAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (searchAdapter.searchList[position].type) {
                    0 -> {
                        val bundle =
                            bundleOf("Product" to searchAdapter.searchList[position].product)
                        findNavController()
                            .navigate(R.id.action_searchFragment_to_productFragment, bundle)
                    }
                    1 -> {
                        val bundle =
                            bundleOf("CategoryName" to searchAdapter.searchList[position].title)
                        findNavController()
                            .navigate(R.id.action_searchFragment_to_allProductsFragment, bundle)
                    }
                    2 -> {
                        val bundle = bundleOf("ShopId" to searchAdapter.searchList[position].id)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_shopFragment,
                            bundle
                        )
                    }
                }
            }
        })
        return binding.root
    }

    private fun observerSearch() {
        searchViewModel.searchData.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = it.data!!
                    if (data.products.isNullOrEmpty() && data.shops.isNullOrEmpty() && data.categoryProds.isNullOrEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                        binding.searchRecyclerView.visibility = View.GONE
                    } else updateUI(it.data)
                }
                is ApiResponse.Loading -> {
                    binding.empty.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ApiResponse.TokenExpire -> {
                    Toast.makeText(requireContext(), "generateToken expire", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("access generateToken ", "ACC_TOKEN is $ACC_TOKEN")
                    lifecycleScope.launch {
                        generateToken(requireContext())
                    }
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUI(data: SearchResult) {
        searchList.clear()
        data.products?.forEach { item ->
            searchList.add(
                SearchItem(
                    id = item.id,
                    title = item.title,
                    type = 0,
                    imageUrl = item.imgUrls[0].imageUrl,
                    product = item
                )
            )
        }

        data.categoryProds?.forEach { item ->
            searchList.add(SearchItem(id = item.id, title = item.category, type = 1))
        }

        data.shops?.forEach { item ->
            searchList.add(
                SearchItem(
                    id = item.id,
                    title = item.shopName,
                    type = 2,
                    imageUrl = item.imgUrls[0].imageUrl
                )
            )
        }
        searchAdapter.updateSearchList(searchList)
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateToken.observe(viewLifecycleOwner, {
            if (it == null) {
                findNavController().navigate(R.id.action_addressFragment_to_authActivity)
                activity?.finish()
            } else {
                onAttach(requireContext())
            }
        })

    }

    private fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.requestFocus()
        binding.searchEditText.showKeyboard()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(SearchRepository(ServiceBuilder.buildService(ACC_TOKEN)))
        )[SearchViewModel::class.java]
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
