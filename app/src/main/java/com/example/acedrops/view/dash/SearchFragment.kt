package com.example.acedrops.view.dash

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.SearchAdapter
import com.example.acedrops.databinding.FragmentSearchBinding
import com.example.acedrops.model.search.SearchItem
import com.example.acedrops.model.search.SearchResult
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.SearchViewModel

class SearchFragment : Fragment() {
    private var  mLastClickTime:Long = 0
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    private val searchAdapter = SearchAdapter()
    private var searchList = mutableListOf<SearchItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[SearchViewModel::class.java]

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!s.isNullOrBlank())
                    searchViewModel.getSearch(s.toString(), requireContext())
                        .observe(viewLifecycleOwner, {
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
                                is ApiResponse.Error -> Toast.makeText(
                                    requireContext(),
                                    it.errorMessage ?: "Something went wrong!!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        searchAdapter.setOnItemClickListener(object : SearchAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return
                } else {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
