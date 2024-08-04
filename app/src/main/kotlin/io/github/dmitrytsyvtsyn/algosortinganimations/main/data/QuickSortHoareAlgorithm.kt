package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class QuickSortHoareAlgorithm : SortingAlgorithm {

    override val title = R.string.quick_sort_hoare
    override val description = R.string.quick_sort_hoare

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
        if (array.isEmpty()) return
        if (start >= end) return

        var pivotIndex = start + (end - start) / 2
        val pivot = array[pivotIndex]

        steps.add(
            SortingAlgorithmStep.SelectRange(
                startIndex = start,
                endIndex = end,
                title = resources.getString(R.string.quick_sort_selecting_range)
            )
        )

        steps.add(
            SortingAlgorithmStep.Select(
                indices = intArrayOf(pivotIndex),
                type = SortingAlgorithmStep.Select.Type.BORDER,
                title = resources.getString(R.string.quick_sort_selecting_pivot_number)
            )
        )

        var i = start
        var j = end
        while (i <= j) {

            if (i != pivotIndex) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(i),
                        title = resources.getString(R.string.quick_sort_compare_number_from_beginning_with_pivot, array[i])
                    )
                )
            }

            while (array[i] < pivot) {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i),
                        title = resources.getString(R.string.quick_sort_number_is_less_than_pivot_nothing_to_do, array[i])
                    )
                )

                i++

                if (i != pivotIndex) {
                    steps.add(
                        SortingAlgorithmStep.Select(
                            indices = intArrayOf(i),
                            title = resources.getString(R.string.quick_sort_compare_number_from_beginning_with_pivot, array[i])
                        )
                    )
                }
            }

            if (i != pivotIndex) {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i),
                        title = resources.getString(R.string.quick_sort_number_is_more_than_pivot_leave_for_exchange, array[i])
                    )
                )
            }

            if (j != pivotIndex) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(j),
                        title = resources.getString(R.string.quick_sort_compare_number_from_end_with_pivot, array[j])
                    )
                )
            }

            while (array[j] > pivot) {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(j),
                        title = resources.getString(R.string.quick_sort_number_is_more_than_pivot_nothing_to_do, array[j])
                    )
                )

                j--

                if (j != pivotIndex) {
                    steps.add(
                        SortingAlgorithmStep.Select(
                            indices = intArrayOf(j),
                            title = resources.getString(R.string.quick_sort_compare_number_from_end_with_pivot, array[j])
                        )
                    )
                }
            }

            if (j != pivotIndex) {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(j),
                        title = resources.getString(R.string.quick_sort_number_is_less_than_pivot_leave_for_exchange, array[j])
                    )
                )
            }

            if (i > j) break

            val (newPivotIndex, title) = array.calculateTitleWithPivot(i, j, pivotIndex, resources)

            if (newPivotIndex != -1) {
                pivotIndex = newPivotIndex
            }

            if (title.isNotEmpty()) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(i, j),
                        title = title
                    )
                )
                steps.add(
                    SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.Swap(
                                index1 = i,
                                index2 = j,
                            ),
                            SortingAlgorithmStep.Unselect(
                                indices = intArrayOf(i, j)
                            )
                        ),
                        title = resources.getString(R.string.quick_sort_swapping_numbers)
                    )
                )
            }

            val tmp = array[i]
            array[i] = array[j]
            array[j] = tmp

            i++
            j--
        }

        steps.add(
            SortingAlgorithmStep.List(
                steps = arrayOf(
                    SortingAlgorithmStep.UnselectRange(
                        startIndex = start,
                        endIndex = end,
                    ),
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(pivotIndex),
                        type = SortingAlgorithmStep.Unselect.Type.BORDER,
                        title = resources.getString(R.string.quick_sort_selecting_pivot_number)
                    )
                ),
                title = resources.getString(R.string.quick_sort_array_was_sorted_successfully)
            )
        )

        if (i < end) {
            sortRecursive(steps, array, resources, i, end)
        }

        if (start < j) {
            sortRecursive(steps, array, resources, start, j)
        }
    }

    private fun IntArray.calculateTitleWithPivot(
        i: Int, j: Int, pivot: Int,
        resources: Resources
    ): Pair<Int, String> {

        return when {
            i == j -> {
                -1 to ""
            }
            i == pivot -> {
                j to resources.getString(R.string.quick_sort_pivot_and_number_should_be_swapped, this[j])
            }
            j == pivot -> {
                i to resources.getString(R.string.quick_sort_pivot_and_number_should_be_swapped, this[i])
            }
            else -> {
                -1 to resources.getString(R.string.quick_sort_numbers_should_be_swapped, this[i], this[j])
            }
        }
    }

}