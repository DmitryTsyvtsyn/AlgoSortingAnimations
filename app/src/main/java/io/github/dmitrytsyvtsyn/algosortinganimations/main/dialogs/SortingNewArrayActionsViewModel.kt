package io.github.dmitrytsyvtsyn.algosortinganimations.main.dialogs

import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.RandomArraysProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingNewArrayActionsViewModel(
    private val producer: RandomArraysProducer = RandomArraysProducer(6)
) {

    private val _state = MutableStateFlow(
        SortingNewArrayActionsState(
            size = 6,
            sizes = intArrayOf(2, 3, 4, 5, 6),
            array = producer.randomArray(6),
            sizesChanged = true
        )
    )
    val state = _state.asStateFlow()

    fun changeSize(size: Int) = updateState {
        copyWithChangedSize(size)
    }

    fun generateRandomArray() = updateState {
        copyWithChangedArray(producer.randomArray(size))
    }

    fun generateSortedArray() = updateState {
        copyWithChangedArray(producer.randomArray(size).sortedArray())
    }

    private inline fun updateState(newState: SortingNewArrayActionsState.() -> SortingNewArrayActionsState) {
        _state.value = _state.value.newState()
    }

}