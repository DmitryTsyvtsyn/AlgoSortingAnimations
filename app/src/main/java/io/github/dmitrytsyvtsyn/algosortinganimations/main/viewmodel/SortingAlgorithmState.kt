package io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.EmptyAlgorithm
import io.github.dmitrytsyvtsyn.algosortinganimations.main.data.SortingAlgorithm

class SortingAlgorithmState(
    val selectedAlgorithm: SortingAlgorithm = EmptyAlgorithm(),
    val buttonState: SortingAnimationButtonState = SortingAnimationButtonState.NONE,
    val sortingArray: IntArray = intArrayOf(),
    private val compared: Int = 7 // low byte: 00000111
) {

    fun hasChanged(pieceState: Int): Boolean = (compared and pieceState) == pieceState

    fun difference(other: SortingAlgorithmState): SortingAlgorithmState {
        var compared = 0

        if (selectedAlgorithm != other.selectedAlgorithm) {
            compared = compared or selectedAlgorithmChanged
        }

        if (buttonState != other.buttonState) {
            compared = compared or buttonStateChanged
        }

        if (!sortingArray.contentEquals(other.sortingArray)) {
            compared = compared or sortingArrayChanged
        }

        return SortingAlgorithmState(selectedAlgorithm, buttonState, sortingArray, compared)
    }

    fun steps(resources: Resources): List<SortingAlgorithmStep> {
        return selectedAlgorithm.sort(sortingArray.copyOf(), resources)
    }

    fun changedWith(selectedAlgorithm: SortingAlgorithm) =
        SortingAlgorithmState(selectedAlgorithm, buttonState, sortingArray)

    fun changedWith(buttonState: SortingAnimationButtonState) =
        SortingAlgorithmState(selectedAlgorithm, buttonState, sortingArray)

    fun changedWith(sortingArray: IntArray) =
        SortingAlgorithmState(selectedAlgorithm, buttonState, sortingArray)

    companion object {
        const val selectedAlgorithmChanged: Int = 1 // low byte: 00000001
        const val buttonStateChanged: Int = 2 // low byte: 00000010
        const val sortingArrayChanged: Int = 4 // low byte: 00000100
    }
}