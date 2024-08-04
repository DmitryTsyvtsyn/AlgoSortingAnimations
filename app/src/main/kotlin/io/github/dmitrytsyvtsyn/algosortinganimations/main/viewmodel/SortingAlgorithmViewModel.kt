package io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel

import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.CoreViewModel
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSortAlgorithm
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.RandomArraysProducer
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SortingAlgorithm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingAlgorithmViewModel(
    producer: RandomArraysProducer = RandomArraysProducer(6)
) : CoreViewModel {

    private val _state = MutableStateFlow(SortingAlgorithmState(
        selectedAlgorithm = BubbleSortAlgorithm(),
        buttonState = SortingAnimationButtonState.PAUSED,
        sortingArray = producer.randomArray(6)
    ))
    val state = _state.asStateFlow()

    val sortingArray: IntArray get() = _state.value.sortingArray.copyOf()
    
    fun changeSortingAlgorithm(algorithm: SortingAlgorithm) = updateState {
        changedWith(algorithm)
    }

    fun changeSortingArray(array: IntArray) = updateState {
        changedWith(array)
    }

    fun toggleAnimation() = updateState {
        changedWith(
            when (buttonState) {
                SortingAnimationButtonState.NONE,
                SortingAnimationButtonState.PAUSED -> SortingAnimationButtonState.RUNNING
                SortingAnimationButtonState.RUNNING -> SortingAnimationButtonState.PAUSED
            }
        )
    }

    private inline fun updateState(newState: SortingAlgorithmState.() -> SortingAlgorithmState) {
        _state.value = _state.value.newState()
    }

}