package io.github.dmitrytsyvtsyn.algosortinganimations.main

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.BubbleSort
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SortingAlgorithm
import kotlin.random.Random

class SortingAlgorithmState(
    val selectedAlgorithm: SortingAlgorithm = BubbleSort(),
    private val sortingArray: IntArray = IntArray(6) { Random.nextInt(100) }
) {
    val elements: IntArray
        get() = sortingArray.copyOf()

    fun steps(resources: Resources): List<SortingAlgorithmStep> {
        return selectedAlgorithm.sort(sortingArray, resources)
    }

    fun copyWithSelectedAlgorithm(selectedAlgorithm: SortingAlgorithm) =
        SortingAlgorithmState(selectedAlgorithm, sortingArray)

    fun copyWithUpdatedSortingArray(): SortingAlgorithmState {
        val newSortingArray = IntArray(sortingArray.size) {
            Random.nextInt(100)
        }
        return SortingAlgorithmState(selectedAlgorithm, newSortingArray)
    }
}