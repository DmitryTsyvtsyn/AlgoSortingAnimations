package io.github.dmitrytsyvtsyn.algosortinganimations.main.dialogs

import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.CoreViewModel
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.RandomArraysProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingNewArrayActionsViewModel(
    private val producer: RandomArraysProducer = RandomArraysProducer(6),
    array: IntArray = producer.randomArray(6),
) : CoreViewModel {

    private val _state = MutableStateFlow(
        SortingNewArrayActionsState(
            size = array.size,
            sizes = intArrayOf(2, 3, 4, 5, 6),
            array = array,
            sizesChanged = true
        )
    )
    val state = _state.asStateFlow()

    val array: IntArray
        get() = _state.value.array

    fun changeSize(size: Int) = updateState {
        copyWithChangedSize(size)
    }

    fun generateRandomArray() = updateState {
        copyWithChangedArray(producer.randomArray(size))
    }

    fun generateSortedArray() = updateState {
        copyWithChangedArray(producer.randomArray(size).sortedArray())
    }

    fun navigateBack() = updateState {
        copyWithNavigatedBack(backNavigated = true)
    }

    private inline fun updateState(newState: SortingNewArrayActionsState.() -> SortingNewArrayActionsState) {
        _state.value = _state.value.newState()
    }

}