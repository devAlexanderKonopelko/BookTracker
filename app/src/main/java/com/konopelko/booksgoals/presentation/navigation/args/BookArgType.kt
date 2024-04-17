package com.konopelko.booksgoals.presentation.navigation.args

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.konopelko.booksgoals.domain.model.book.Book

class BookArgType : NavType<Book>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): Book? = bundle.getParcelable(key)

    override fun parseValue(value: String): Book = Gson().fromJson(value, Book::class.java)

    override fun put(bundle: Bundle, key: String, value: Book) {
        bundle.putParcelable(key, value)
    }
}