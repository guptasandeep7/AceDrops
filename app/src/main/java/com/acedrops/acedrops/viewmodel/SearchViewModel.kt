package com.acedrops.acedrops.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acedrops.acedrops.model.search.SearchResult
import com.acedrops.acedrops.repository.SearchRepository
import com.acedrops.acedrops.utill.ApiResponse
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private var _searchData: MutableLiveData<ApiResponse<SearchResult>> = MutableLiveData()

    fun getSearch(text: String,context: Context): MutableLiveData<ApiResponse<SearchResult>> {
        viewModelScope.launch {
            _searchData = SearchRepository().postSearch(text, context)
        }
        return _searchData
    }

}
