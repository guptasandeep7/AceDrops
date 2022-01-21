package com.example.acedrops.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acedrops.model.search.SearchResult
import com.example.acedrops.repository.dashboard.SearchRepository
import com.example.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class SearchViewModel(val repository: SearchRepository) : ViewModel() {

    private var _searchData:MutableLiveData<ApiResponse<SearchResult>> = MutableLiveData()
    val searchData: LiveData<ApiResponse<SearchResult>>
        get() = _searchData

    fun getSearch(text:String) = viewModelScope.launch {
        _searchData = repository.postSearch(text)
    }

}
