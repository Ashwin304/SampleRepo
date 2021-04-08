package com.example.sampleloginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleloginapp.io.repository.NewsRepository

class NewsDetailViewModelFactory(): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsDetailViewModel() as T
    }
}