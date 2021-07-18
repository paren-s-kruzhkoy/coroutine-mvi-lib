package com.example.coroutine_mvi_lib

interface MviView<Action, State: Any> {
    val reducer: MviReducer<Action, State>

    fun reduce(action: Action) = reducer.reduce(action)
    fun renderState(oldStates: List<State>, newState: State)
    fun renderEvent(event: Any)
}