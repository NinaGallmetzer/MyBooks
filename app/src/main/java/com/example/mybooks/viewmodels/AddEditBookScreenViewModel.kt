package com.example.mybooks.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.Book
import com.example.mybooks.data.BookRepository
import kotlinx.coroutines.launch
import java.util.*

class AddEditBookScreenViewModel(private val bookRepository: BookRepository, private val bookId: Int = 0): ViewModel() {

    var bookToAdd by mutableStateOf(Book())
        private set

    init {
        viewModelScope.launch {
            if (bookId == 0) {
                bookToAdd = Book()
            } else {
                bookRepository.getBookById(bookId).collect { book ->
                    bookToAdd = book    // copy the book if it is not null
                }
            }
        }
    }

    // update the bookToAdd state based on UI onValueChange events
    fun updateBook(newBook: Book) {
        bookToAdd = newBook
    }

    // use the already up to date book variable
    suspend fun saveBook(){
        // check if we need to update the book or add a new one
        if(bookId == 0) {
            bookRepository.addBook(bookToAdd)
        } else {
            bookRepository.updateBook(bookToAdd)
        }
    }

    fun isValidBook(title: String, author: String, firstPublished: Int, isbn: String): Boolean {
        return (isValidTitle(title) && isValidAuthor(author) && isValidFirstPublished(firstPublished) && isValidISBN(isbn))
    }

    fun isValidTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    fun isValidAuthor(author: String): Boolean {
        return author.isNotBlank()
    }

    fun isValidFirstPublished(firstPublished: Int): Boolean {
        return firstPublished in 0 .. Calendar.getInstance().get(Calendar.YEAR)
    }

    fun isValidISBN(isbn: String): Boolean {
        val cleanISBN = isbn.replace("-", "").replace(" ", "")

        if (cleanISBN.length != 13) {
            return false
        }

        var sum = 0
        for (i in 0 until 12) {
            val digit = cleanISBN[i].code - '0'.code
            sum += if (i % 2 == 0) digit else digit * 3
        }

        val checkDigit = (10 - (sum % 10)) % 10
        val lastDigit = cleanISBN[12].code - '0'.code
        return checkDigit == lastDigit
    }
}