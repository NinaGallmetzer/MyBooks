@file:Suppress("UNCHECKED_CAST")

package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybooks.data.BookRepository

class HomeScreenViewModelFactory (private val repository : BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)){
            return HomeScreenViewModel(bookRepository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
