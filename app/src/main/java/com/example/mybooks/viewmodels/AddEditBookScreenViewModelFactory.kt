package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybooks.data.BookRepository

class AddEditBookScreenViewModelFactory (private val repository : BookRepository, private val bookId: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddEditBookScreenViewModel::class.java)){
            return AddEditBookScreenViewModel(bookRepository = repository, bookId = bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
