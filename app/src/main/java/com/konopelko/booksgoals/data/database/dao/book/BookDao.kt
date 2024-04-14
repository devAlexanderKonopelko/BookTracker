package com.konopelko.booksgoals.data.database.dao.book

import androidx.room.Dao
import androidx.room.Insert
import com.konopelko.booksgoals.data.database.entity.book.BookEntity

@Dao
interface BookDao {

    @Insert
    fun addBook(book: BookEntity)
}