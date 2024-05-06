package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class QuickSortLomutoAlgorithm : SortingAlgorithm {

    override val title = R.string.quick_sort_lomuto
    override val description = R.string.quick_sort_lomuto

    override val worstTimeComplexity = "n²"
    override val bestTimeComplexity = "O(nlog(n))"
    override val averageTimeComplexity = "O(nlog(n))"
    override val worstSpaceComplexity = "O(log(n))"

    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        sortRecursive(steps, array, resources)

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))

        return steps
    }

    private fun sortRecursive(
        steps: MutableList<SortingAlgorithmStep>,
        array: IntArray,
        resources: Resources,
        start: Int = 0,
        end: Int = array.size - 1
    ) {
        var pivotIndex = end
        val pivotValue = array[pivotIndex]

        steps.add(
            SortingAlgorithmStep.SelectRange(
                startIndex = start,
                endIndex = end,
                title = resources.getString(R.string.quick_sort_selecting_range)
            )
        )

        steps.add(
            SortingAlgorithmStep.Select(
                indices = intArrayOf(end),
                type = SortingAlgorithmStep.Select.Type.BORDER,
                title = resources.getString(R.string.quick_sort_selecting_pivot_number)
            )
        )

        var i = start
        var j = start - 1
        while (i <= end) {

            steps.add(
                SortingAlgorithmStep.Select(
                    indices = intArrayOf(i),
                    title = resources.getString(R.string.quick_sort_go_through_array_with_index_i)
                )
            )

            steps.add(
                SortingAlgorithmStep.Select(
                    indices = intArrayOf(pivotIndex),
                    title = resources.getString(R.string.quick_sort_comparing_number_at_index_i_with_pivot, array[i])
                )
            )

            if (array[i] <= pivotValue) {

                if (pivotIndex != i) {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(pivotIndex),
                            title = resources.getString(R.string.quick_sort_number_at_index_i_is_less_or_equal_pivot, array[i])
                        )
                    )
                }

                j++

                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(j),
                        title = resources.getString(R.string.quick_sort_go_through_array_with_index_j)
                    )
                )

                if (i != j) {

                    pivotIndex = when {
                        i == pivotIndex -> j
                        j == pivotIndex -> i
                        else -> pivotIndex
                    }

                    val tmp = array[i]
                    array[i] = array[j]
                    array[j] = tmp

                    steps.add(SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.Swap(
                                index1 = i,
                                index2 = j
                            ),
                            SortingAlgorithmStep.Unselect(
                                indices = intArrayOf(i, j)
                            )
                        ),
                        title = resources.getString(R.string.quick_sort_indices_are_not_equal)
                    ))
                } else {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(i, j),
                            title = resources.getString(R.string.quick_sort_indices_are_equal)
                        )
                    )
                }
            } else {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i, pivotIndex),
                        title = resources.getString(R.string.quick_sort_number_at_index_i_is_more_pivot, array[i])
                    )
                )
            }

            i++
        }

        steps.add(
            SortingAlgorithmStep.List(
                steps = arrayOf(
                    SortingAlgorithmStep.UnselectRange(
                        startIndex = start,
                        endIndex = end,
                    ),
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(j),
                        type = SortingAlgorithmStep.Unselect.Type.BORDER,
                        title = resources.getString(R.string.quick_sort_selecting_pivot_number)
                    )
                ),
                title = resources.getString(R.string.quick_sort_array_was_sorted_successfully)
            )
        )

        if (j - start >= 2) {
            sortRecursive(steps, array, resources, start, j - 1)
        }

        if (end - j >= 2) {
            sortRecursive(steps, array, resources, j + 1, end)
        }
    }

}