package com.example.mybooks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Insert
    fun insertOnCreate(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * from Book ORDER BY title ASC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * from Book WHERE bookId = :bookId")
    fun getBookById(bookId: String): Flow<Book>
}