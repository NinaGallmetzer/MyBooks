package com.example.mybooks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class MyBooksDB : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var Instance : MyBooksDB ? = null

        fun getDatabase (context: Context): MyBooksDB {
            return Instance ?: synchronized(this) {

                Room.databaseBuilder(context, MyBooksDB::class.java, "my_books_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Thread { prepopulateDb(getDatabase(context)) }.start()
                        }
                    })
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }

        private fun prepopulateDb(db: MyBooksDB) {
            for (book in getInitialBooks()) {
                db.bookDao().insertOnCreate(book)
            }
        }
    }
}