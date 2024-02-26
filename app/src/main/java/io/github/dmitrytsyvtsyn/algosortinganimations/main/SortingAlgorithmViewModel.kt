package io.github.dmitrytsyvtsyn.algosortinganimations.main

import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSort
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSortImproved
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SortingAlgorithm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingAlgorithmViewModel {

    private val _algorithmListState = MutableStateFlow(listOf(
        BubbleSort(),
        BubbleSortImproved()
    ))
    val algorithmListState = _algorithmListState.asStateFlow()

    private val _algorithmDetailState = MutableStateFlow(SortingAlgorithmState())
    val algorithmDetailState = _algorithmDetailState.asStateFlow()

    private val _buttonState = MutableStateFlow(SortingAnimationButtonState.PAUSED)
    val buttonState = _buttonState.asStateFlow()

    fun changeSortingAlgorithm(algorithm: SortingAlgorithm) {
        _algorithmDetailState.value = _algorithmDetailState.value.copyWithSelectedAlgorithm(algorithm)
    }

    fun generateRandomArray() {
        _algorithmDetailState.value = _algorithmDetailState.value.copyWithUpdatedSortingArray()
    }

    fun toggleAnimation() {
        _buttonState.value = when (_buttonState.value) {
            SortingAnimationButtonState.PAUSED -> SortingAnimationButtonState.RUNNING
            SortingAnimationButtonState.RUNNING -> SortingAnimationButtonState.PAUSED
        }
    }

}