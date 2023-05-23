package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.Book
import com.example.mybooks.data.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val bookRepository: BookRepository): ViewModel() {

    private val _books = MutableStateFlow(listOf<Book>())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect{ bookList ->
                if(bookList.isNotEmpty()) {
                    _books.value = bookList
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
