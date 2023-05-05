package com.example.mybooks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.Book
import com.example.mybooks.data.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AddEditBookScreenViewModel(private val bookRepository: BookRepository, bookId: String = ""): ViewModel() {

    private val _book = MutableStateFlow(Book())
    val book: StateFlow<Book> = _book.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getBookById(bookId).collect() { book ->
                _book.value = book
            }
        }
    }

    // TODO: add validation of ISBN
    fun isValidBook(title: String, author: String, firstPublished: Int, isbn: String): Boolean {
        return (title.isNotBlank() && author.isNotBlank() && firstPublished in 0 .. Calendar.getInstance().get(Calendar.YEAR) && isbn.isNotBlank())
    }

    suspend fun saveBook(title: String, author: String, firstPublished: Int, isbn: String) {
        if(isValidBook(title, author, firstPublished, isbn)) {
            if (book.value.bookId == "") {
                val newBook = Book(
                    title = title,
                    author = author,
                    firstPublished = firstPublished,
                    isbn = isbn
                )
                bookRepository.addBook(newBook)
            } else {
                val updatedBook = Book(
                    bookId = book.value.bookId,
                    title = title,
                    author = author,
                    firstPublished = firstPublished,
                    isbn = isbn
                )
                bookRepository.updateBook(updatedBook)
            }
        }
    }
}
