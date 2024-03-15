package io.github.dmitrytsyvtsyn.algosortinganimations.new_array

import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.CoreViewModel
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.RandomArraysProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingNewArrayViewModel(
    private val producer: RandomArraysProducer = RandomArraysProducer(6),
    array: IntArray = producer.randomArray(6),
) : CoreViewModel {

    private val _state = MutableStateFlow(
        SortingNewArrayState(
            size = array.size,
            sizes = intArrayOf(2, 3, 4, 5, 6),
            array = array
        )
    )
    val state = _state.asStateFlow()

    val array: IntArray
        get() = _state.value.array.copyOf()

    fun changeSize(size: Int) = updateState {
        changedWith(size)
    }

    fun generateRandomArray() = updateState {
        changedWith(producer.randomArray(size))
    }

    fun generateSortedArray() = updateState {
        changedWith(producer.randomArray(size).sortedArray())
    }

    fun navigateBack() = updateState {
        changedWith(backNavigated = true)
    }

    private inline fun updateState(newState: SortingNewArrayState.() -> SortingNewArrayState) {
        _state.value = _state.value.newState()
    }

}