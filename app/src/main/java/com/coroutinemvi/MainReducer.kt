package com.coroutinemvi

import com.example.coroutine_mvi_lib.MviReducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainReducer: MviReducer<MainAction, MainState>() {
    override fun initialState(): MainState = MainState()

    override fun transformState(action: MainAction): Flow<MainState> {
        return when(action) {
            is MainAction.Increment -> flow {
                val oldState = lastState
                val newList = mutableListOf<String>()
                newList.addAll(oldState.recyclerList)
                newList.add((oldState.recyclerList.size + 1).toString())
                val newState = MainState(counter = newList.size.toString(), recyclerList = newList)
                emit(newState)
            }
            is MainAction.Decrement -> flow {
                val oldState = lastState
                if (oldState.recyclerList.isNotEmpty()) {
                    val newList = oldState.recyclerList.toMutableList()
                    newList.removeLast()
                    val newCounter = oldState.recyclerList.size.toString()
                    val newState = MainState(counter = newCounter, recyclerList = newList)
                    emit(newState)
                }
            }
        }
    }
}