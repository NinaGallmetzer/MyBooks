@file:Suppress("UNCHECKED_CAST")

package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybooks.data.BookRepository

class AddBookScreenViewModelFactory (private val repository : BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddBookScreenViewModel::class.java)){
            return AddBookScreenViewModel(bookRepository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
