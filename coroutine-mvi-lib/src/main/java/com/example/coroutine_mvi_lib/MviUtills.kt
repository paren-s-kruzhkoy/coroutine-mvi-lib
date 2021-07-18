package com.example.coroutine_mvi_lib

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

fun <Action> View.reduceOnClick(mviView: MviView<Action, *>, action: Action) {
    setOnClickListener { mviView.reduce(action) }
}

fun showSnackbarOnEvent(view: View, event: Any) {
    when (event) {
        is String -> {
            showSnackBar(view, event, backgroundColor = Color.WHITE, textColor = Color.BLACK)
        }
        is Throwable -> {
            val msg = when {
                event.message != null -> event.message!!
                event.localizedMessage != null -> event.localizedMessage!!
                else -> return
            }

            showSnackBar(view, msg, backgroundColor = Color.RED, textColor = Color.WHITE)
        }
    }
}

private fun showSnackBar(
    view: View,
    msg: String,
    @ColorInt backgroundColor: Int? = null,
    @ColorInt textColor: Int? = null
) {
    val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    backgroundColor?.let { snackbar.setBackgroundTint(it) }
    textColor?.let { snackbar.setTextColor(it) }
    snackbar.show()
}