package com.codetron.newsapp.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.codetron.newsapp.model.ErrorData
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

fun Float.toDp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
    )
}

fun Float.toSp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
    )
}

fun ResponseBody?.errorMessage(): String? {
    val adapter = Moshi.Builder().build().adapter(ErrorData::class.java)
    val errorBody = adapter.fromJson(this?.string().toString())
    return errorBody?.message
}