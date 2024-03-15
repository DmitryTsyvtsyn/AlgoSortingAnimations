package io.github.dmitrytsyvtsyn.algosortinganimations.selection

import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.CoreViewModel
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSortAlgorithm
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSortBetterAlgorithm
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SelectionSortAlgorithm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SortingAlgorithmSelectionViewModel : CoreViewModel {

    private val _state = MutableStateFlow(listOf(
        BubbleSortAlgorithm(),
        BubbleSortBetterAlgorithm(),
        SelectionSortAlgorithm()
    ))
    val state = _state.asStateFlow()

}