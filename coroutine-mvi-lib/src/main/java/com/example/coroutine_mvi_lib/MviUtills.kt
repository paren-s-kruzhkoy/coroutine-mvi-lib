package com.example.coroutine_mvi_lib

import android.view.View

fun <Action> View.reduceOnClick(mviView: MviView<Action, *>, action: Action) {
    setOnClickListener { mviView.reduce(action) }
}