package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class BubbleSortImproved : SortingAlgorithm {

    override val title = R.string.bubble_sort_improved
    override val description = R.string.bubble_sort

    override val worstTimeComplexity = "O(n²)"
    override val bestTimeComplexity = "O(n)"
    override val averageTimeComplexity = "O(n²)"
    override val worstSpaceComplexity = "O(1)"

    /**
     * worst time: n²
     * best time: n
     * average time: n²
     *
     * amount of memory: 1
     */
    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        val arraySize = array.size
        var isSorted = true
        for (i in 0 until arraySize - 1) {
            for (j in 0 until arraySize - 1 - i) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(j, j + 1),
                        titleResource = resources.getString(
                            R.string.comparing_number,
                            array[j],
                            array[j + 1]
                        )
                    )
                )
                if (array[j] > array[j + 1]) {
                    steps.add(
                        SortingAlgorithmStep.Swap(
                            index1 = j,
                            index2 = j + 1,
                            titleResource = resources.getString(
                                R.string.swaping_numbers,
                                array[j],
                                array[j + 1],
                                array[j],
                                array[j + 1]
                            )
                        )
                    )
                    val tmp = array[j + 1]
                    array[j + 1] = array[j]
                    array[j] = tmp

                    isSorted = false

                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j, j + 1),
                            titleResource = resources.getString(
                                R.string.numbers_was_replaced,
                                array[j + 1],
                                array[j]
                            )
                        )
                    )
                } else {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j, j + 1),
                            titleResource = resources.getString(
                                R.string.numbers_are_ordered,
                                array[j],
                                array[j + 1]
                            )
                        )
                    )
                }
            }
            if (isSorted) break
        }

        if (isSorted) {
            steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted_1)))
        } else {
            steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted_0)))
        }

        return steps
    }

}