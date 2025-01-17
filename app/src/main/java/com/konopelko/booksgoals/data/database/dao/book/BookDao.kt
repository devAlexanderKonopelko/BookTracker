package com.konopelko.booksgoals.data.database.dao.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.konopelko.booksgoals.data.database.entity.book.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): Flow<BookEntity>

    @Query("SELECT * FROM books WHERE is_finished = 1")
    fun getFinishedBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE is_started = 0 AND is_finished = 0")
    fun getNotStartedUnfinishedBooks(): List<BookEntity>

    @Insert
    fun addBook(book: BookEntity): Long

    @Query("UPDATE books SET is_started = :isStarted WHERE id = :bookId")
    fun updateBookIsStarted(isStarted: Boolean, bookId: Int)

    @Query(
        "UPDATE books " +
        "SET is_finished = :isFinished, " +
        "finish_date = :finishDate  " +
        "WHERE id = :bookId"
    )
    fun updateBookIsFinished(isFinished: Boolean, bookId: Int, finishDate: String)

    @Query("DELETE FROM books WHERE id = :bookId")
    fun deleteBook(bookId: Int)
}