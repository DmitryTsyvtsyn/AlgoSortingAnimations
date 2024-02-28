package io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSort
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SortingAlgorithm

class SortingAlgorithmState(
    val selectedAlgorithm: SortingAlgorithm = BubbleSort(),
    private val sortingArray: IntArray = intArrayOf()
) {
    val elements: IntArray
        get() = sortingArray.copyOf()

    fun steps(resources: Resources): List<SortingAlgorithmStep> {
        return selectedAlgorithm.sort(sortingArray, resources)
    }

    fun copyWithSelectedAlgorithm(selectedAlgorithm: SortingAlgorithm) =
        SortingAlgorithmState(selectedAlgorithm, sortingArray)

    fun copyWithNewSortingArray(newArray: IntArray) =
        SortingAlgorithmState(selectedAlgorithm, newArray)
}