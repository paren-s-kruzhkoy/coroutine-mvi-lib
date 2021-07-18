package com.example.coroutine_mvi_lib

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

abstract class MviFragment<Action, State : Any>(@LayoutRes val contentLayoutId: Int) : Fragment(contentLayoutId), MviView<Action, State> {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reducer.subscribe(lifecycleScope, ::renderState, ::renderEvent)
    }

    override fun renderEvent(event: Any) {
        showSnackbarOnEvent(requireView(), event)
    }
}