package com.example.acedrops.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acedrops.repository.dashboard.SearchRepository

class SearchViewModelFactory(private val searchRepository: SearchRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SearchRepository::class.java).newInstance(searchRepository)
    }
}