package com.example.coroutine_mvi_lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class MviReducer<Action, State : Any>(
    private val statesCapacity: Int = 10,
    private val eventDebounce: Long = 100
) : ViewModel() {
    private val stateConsumer = MutableSharedFlow<State>(
        replay = statesCapacity,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val eventConsumer = Channel<Any>()
    protected val oldStates get() = stateConsumer.replayCache
    protected val lastState get() = oldStates.last()

    init {
        check(statesCapacity > 0){ "States capacity not be a <= 0" }
        stateConsumer.tryEmit(initialState())
    }

    fun subscribe(
        viewScope: CoroutineScope,
        renderStateCallback: suspend (oldStates: List<State>, newState: State) -> Unit,
        renderEventCallback: suspend (Any) -> Unit
    ) {
        stateConsumer
            .drop(stateConsumer.replayCache.size - 1)
            .onEach { newState -> renderStateCallback(oldStates.dropLast(1), newState) }
            .launchIn(viewScope)
        eventConsumer
            .receiveAsFlow()
            .debounce(eventDebounce)
            .onEach(renderEventCallback)
            .launchIn(viewScope)
    }

    fun reduce(action: Action) {
        transformState(action)
            .onEach { state: State -> stateConsumer.emit(state) }
            .catch { cause: Throwable -> errorHandler(cause) }
            .launchIn(viewModelScope)
    }

    fun resetOldStates() = stateConsumer.resetReplayCache()

    protected abstract fun initialState(): State
    protected abstract fun transformState(action: Action): Flow<State>
    protected suspend fun errorHandler(cause: Throwable) { eventConsumer.send(cause) }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}