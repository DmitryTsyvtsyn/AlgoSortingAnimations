package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class BubbleSortAlgorithm : SortingAlgorithm {

    override val title = R.string.bubble_sort
    override val description = R.string.bubble_sort

    override val worstTimeComplexity = "O(n²)"
    override val bestTimeComplexity = "O(n²)"
    override val averageTimeComplexity = "O(n²)"
    override val worstSpaceComplexity = "O(1)"

    /**
     * worst time: O(n²)
     * best time: O(n²)
     * average time: O(n²)
     *
     * amount of memory: O(1)
     */
    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        val arraySize = array.size
        for (i in 0 until arraySize - 1) {
            steps.add(
                SortingAlgorithmStep.SelectRange(
                    startIndex = 0,
                    endIndex = arraySize - 1 - i,
                    title = resources.getString(R.string.bubble_sort_selecting_range_for_swapping, 0, arraySize - 1 - i)
                )
            )
            for (j in 0 until arraySize - 1 - i) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(j, j + 1),
                        title = resources.getString(
                            R.string.bubble_sort_comparing_number,
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
                            title = resources.getString(
                                R.string.bubble_sort_swapping_numbers,
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

                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j, j + 1),
                            title = resources.getString(
                                R.string.bubble_sort_numbers_was_replaced,
                                array[j + 1],
                                array[j]
                            )
                        )
                    )
                } else {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j, j + 1),
                            title = resources.getString(
                                R.string.bubble_sort_numbers_are_ordered,
                                array[j],
                                array[j + 1]
                            )
                        )
                    )
                }
            }
            steps.add(
                SortingAlgorithmStep.UnselectRange(
                    startIndex = 0,
                    endIndex = arraySize - 1 - i,
                    title = resources.getString(R.string.bubble_sort_last_element_popped_up_like_a_bubble, 0, arraySize - 1 - i)
                )
            )
        }

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))

        return steps
    }

}