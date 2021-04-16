package com.example.sampleloginapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleloginapp.NewsApplication
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.repository.NewsRepository

class NewsViewModelFactory(val app: Application, val repository: NewsRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, repository) as T
    }
}