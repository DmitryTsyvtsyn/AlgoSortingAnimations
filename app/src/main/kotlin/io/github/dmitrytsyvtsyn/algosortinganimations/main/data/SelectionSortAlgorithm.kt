package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class SelectionSortAlgorithm : SortingAlgorithm {

    override val title = R.string.selection_sort
    override val description = R.string.selection_sort

    override val worstTimeComplexity = "O(n²)"
    override val bestTimeComplexity = "O(n²)"
    override val averageTimeComplexity = "O(n²)"
    override val worstSpaceComplexity = "O(1)"

    /**
     * worst time: n²
     * best time: n²
     * average time: n²
     *
     * amount of memory: 1
     */
    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        val arraySize = array.size
        for (i in 0 until arraySize - 1) {
            steps.add(
                SortingAlgorithmStep.SelectRange(
                    startIndex = i,
                    endIndex = i,
                    title = resources.getString(R.string.selection_sort_selecting_minimum_number, i, array[i])
                )
            )
            var min = i
            for (j in i + 1 until arraySize) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(min, j),
                        title = resources.getString(R.string.selection_sort_comparing_minimum_number, array[min], array[j])
                    )
                )

                if (array[min] > array[j]) {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(min),
                            title = resources.getString(R.string.selection_sort_minimum_more_than_new_number, array[j])
                        )
                    )
                    min = j
                } else {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j),
                            title = resources.getString(R.string.selection_sort_minimum_less_than_new_number, array[j])
                        )
                    )
                }
            }

            if (min != i) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(i),
                        title = resources.getString(R.string.selection_sort_new_minimum_has_found, array[min], i)
                    )
                )

                steps.add(
                    SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.UnselectRange(
                                startIndex = i,
                                endIndex = i,
                                force = true
                            ),
                            SortingAlgorithmStep.SelectRange(
                                startIndex = min,
                                endIndex = min,
                                force = true
                            ),
                            SortingAlgorithmStep.Swap(
                                index1 = i,
                                index2 = min
                            )
                        ),
                        title = resources.getString(R.string.selection_sort_new_minimum_has_found, array[min], i)
                    )
                )

                val tmp = array[i]
                array[i] = array[min]
                array[min] = tmp

                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i, min),
                        title = resources.getString(R.string.selection_sort_new_minimum_has_applied, array[i])
                    )
                )
                steps.add(
                    SortingAlgorithmStep.UnselectRange(
                        startIndex = i,
                        endIndex = i,
                        title = resources.getString(R.string.selection_sort_new_minimum_has_applied, array[i])
                    )
                )
            } else {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i),
                        title = resources.getString(R.string.selection_sort_minimum_has_already_set, array[i])
                    )
                )
                steps.add(
                    SortingAlgorithmStep.UnselectRange(
                        startIndex = i,
                        endIndex = i,
                        title = resources.getString(R.string.selection_sort_new_minimum_has_applied, array[i])
                    )
                )
            }
        }

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))

        return steps
    }


}