package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.Book
import com.example.mybooks.data.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val bookRepository: BookRepository): ViewModel() {

    private val _bookList = MutableStateFlow(listOf<Book>())
    val bookList: StateFlow<List<Book>> = _bookList.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect() { bookList ->
                if (bookList.isNotEmpty()) {
                    _bookList.value = bookList
                }
            }
        }
    }

    suspend fun toggleReadState(book: Book) {
        book.read = !book.read
        bookRepository.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        bookRepository.deleteBook(book)
    }
}
