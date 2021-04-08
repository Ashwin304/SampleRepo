package com.example.sampleloginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.repository.NewsRepository

class NewsViewModelFactory(val repository: NewsRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}