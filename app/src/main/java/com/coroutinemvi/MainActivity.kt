package com.coroutinemvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.coroutinemvi.adapter.RecyclerListAdapter
import com.coroutinemvi.databinding.ActivityMainBinding
import com.example.coroutine_mvi_lib.MviReducer
import com.example.coroutine_mvi_lib.MviView
import com.example.coroutine_mvi_lib.reduceOnClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MviView<MainAction, MainState> {
    private val binding by viewBinding(ActivityMainBinding::bind)
    override val reducer: MviReducer<MainAction, MainState> by viewModels<MainReducer>()

    private val recyclerListAdapter = RecyclerListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reducer.subscribe(lifecycleScope, ::renderState, ::renderEvent)

        binding.recyclerList.apply {
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            adapter = recyclerListAdapter
        }
        binding.increment.reduceOnClick(this, MainAction.Increment())
        binding.decrement.reduceOnClick(this, MainAction.Decrement())
    }

    override fun renderState(oldStates: List<MainState>, newState: MainState) {
        val lastState = oldStates.lastOrNull()
        Toast.makeText(this, "New count: ${newState.counter}", Toast.LENGTH_SHORT).show()
        if (lastState != null && lastState.recyclerList.size != newState.recyclerList.size) {
            recyclerListAdapter.data = newState.recyclerList
        }
    }

    override fun renderEvent(event: Any) {
    }
}