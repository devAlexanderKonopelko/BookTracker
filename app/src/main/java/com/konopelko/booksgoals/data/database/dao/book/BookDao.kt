package com.konopelko.booksgoals.data.database.dao.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.konopelko.booksgoals.data.database.entity.book.BookEntity

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE is_finished = 1")
    fun getFinishedBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE is_finished = 0")
    fun getUnfinishedBooks(): List<BookEntity>

    @Insert
    fun addBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    fun deleteBook(bookId: Int)
}