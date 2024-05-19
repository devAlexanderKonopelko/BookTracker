package com.konopelko.booksgoals.presentation.common.utils.uri

import android.net.Uri
import android.os.Parcelable
import com.google.gson.Gson

fun<T: Parcelable> serializeNavParam(param: T): String = Uri.encode(Gson().toJson(param))