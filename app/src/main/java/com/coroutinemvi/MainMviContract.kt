package com.coroutinemvi

sealed class MainAction {
    class Increment() : MainAction()
    class Decrement() : MainAction()
}

data class MainState(
    val counter: String = "0",
    val recyclerList: List<String> = emptyList()
)