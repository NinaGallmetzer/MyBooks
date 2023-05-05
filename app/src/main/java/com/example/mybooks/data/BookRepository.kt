package com.example.mybooks.data

import kotlinx.coroutines.flow.Flow

class BookRepository (private val bookDao: BookDao) {

    suspend fun addBook(book: Book) = bookDao.insert(book = book)

    suspend fun updateBook(book: Book) = bookDao.update(book = book)

    suspend fun deleteBook(book: Book) = bookDao.delete(book = book)

    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    fun getBookById(bookId: Int): Flow<Book> = bookDao.getBookById(bookId)

}