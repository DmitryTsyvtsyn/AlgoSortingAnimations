package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class QuickSortAlgorithm : SortingAlgorithm {

    override val title = R.string.quick_sort
    override val description = R.string.quick_sort

    override val worstTimeComplexity = "n²"
    override val bestTimeComplexity = "O(nlog(n))"
    override val averageTimeComplexity = "O(nlog(n))"
    override val worstSpaceComplexity = "O(log(n))"

    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        sortRecursive(steps, array, array.sortedArray(), resources)

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))

        return steps
    }

    private fun sortRecursive(
        steps: MutableList<SortingAlgorithmStep>,
        array: IntArray,
        sortedArray: IntArray,
        resources: Resources,
        start: Int = 0,
        end: Int = array.size - 1
    ) {
        if (array.isEmpty()) return
        if (start >= end) return

        val pivotIndex = start + (end - start) / 2
        val pivot = array[pivotIndex]
        val newPivotIndex = sortedArray.indexOf(pivot)

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
                title = resources.getString(R.string.quick_sort_selecting_pivot_number)
            )
        )

        steps.add(
            SortingAlgorithmStep.QuickDown(
                currentIndex = pivotIndex,
                newIndex = newPivotIndex,
                title = resources.getString(R.string.quick_sort_selecting_pivot_number)
            )
        )

        steps.add(
            SortingAlgorithmStep.Unselect(
                indices = intArrayOf(pivotIndex),
                title = resources.getString(R.string.quick_sort_selecting_pivot_number)
            )
        )

        var i = start
        var j = end
        while (i <= j) {

            while (array[i] < pivot) {

                steps.addQuickMoveStep(
                    currentIndex = i,
                    newIndex = i,
                    title = resources.getString(R.string.quick_sort_number_is_less_than_pivot, array[i])
                )

                i++
            }

            while (array[j] > pivot) {

                steps.addQuickMoveStep(
                    currentIndex = j,
                    newIndex = j,
                    title = resources.getString(R.string.quick_sort_number_is_more_than_pivot, array[j])
                )

                j--
            }

            if (i <= j) {

                if (i != pivotIndex) {
                    steps.addQuickMoveStep(
                        currentIndex = i,
                        newIndex = j,
                        title = resources.getString(R.string.quick_sort_number_is_more_than_pivot, array[i])
                    )
                }

                if (j != pivotIndex) {
                    steps.addQuickMoveStep(
                        currentIndex = j,
                        newIndex = i,
                        title = resources.getString(R.string.quick_sort_number_is_less_than_pivot, array[j])
                    )
                }

                val tmp = array[i]
                array[i] = array[j]
                array[j] = tmp

                i++
                j--
            }

        }

        steps.add(
            SortingAlgorithmStep.List(
                steps = arrayOf(
                    SortingAlgorithmStep.Quick(),
                    SortingAlgorithmStep.UnselectRange(
                        startIndex = start,
                        endIndex = end
                    )
                ),
                title = resources.getString(R.string.quick_sort_array_was_sorted_successfully)
            )
        )

        if (i < end) {
            sortRecursive(steps, array, sortedArray, resources, i, end)
        }

        if (start < j) {
            sortRecursive(steps, array, sortedArray, resources, start, j)
        }
    }

    private fun MutableList<SortingAlgorithmStep>.addQuickMoveStep(
        currentIndex: Int,
        newIndex: Int,
        title: String
    ) {
        add(
            SortingAlgorithmStep.Select(
                indices = intArrayOf(currentIndex),
                title = title
            )
        )

        add(
            SortingAlgorithmStep.QuickMove(
                currentIndex = currentIndex,
                newIndex = newIndex,
                title = title
            )
        )

        add(
            SortingAlgorithmStep.Unselect(
                indices = intArrayOf(currentIndex),
                title = title
            )
        )
    }

}