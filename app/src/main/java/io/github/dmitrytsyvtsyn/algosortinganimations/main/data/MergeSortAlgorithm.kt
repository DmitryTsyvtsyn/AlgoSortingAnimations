package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep
import kotlin.math.absoluteValue

class MergeSortAlgorithm : SortingAlgorithm {

    override val title = R.string.merge_sort
    override val description = R.string.merge_sort

    override val worstTimeComplexity = "O(nlog(n))"
    override val bestTimeComplexity = "O(nlog(n))"
    override val averageTimeComplexity = "O(nlog(n))"
    override val worstSpaceComplexity = "O(n)"

    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        val arraySize = array.size
        val temporaryArray = array.copyOf()

        steps.add(
            SortingAlgorithmStep.List(
                steps = Array(arraySize - 1) { SortingAlgorithmStep.MergeDivide(pivotIndex = it + 1) },
                title = resources.getString(R.string.merge_sort_divide_array_into_parts)
            )
        )

        var windowSize = 1
        while (windowSize < arraySize) {

            var left = 0
            while (left + windowSize < arraySize) {

                val middle = left + windowSize
                var right = middle + windowSize
                if (right > arraySize) right = arraySize

                var i = left
                var k = left
                var j = middle

                val offsetCount = calcOffsetCount(middle, middle, windowSize, arraySize)

                while (i < middle && j < right) {

                    steps.add(
                        SortingAlgorithmStep.Select(
                            indices = intArrayOf(i, j),
                            title = resources.getString(R.string.merge_sort_comparing_values, array[i], array[j])
                        )
                    )

                    if (array[i] <= array[j]) {
                        steps.add(
                            SortingAlgorithmStep.List(
                                steps = arrayOf(
                                    SortingAlgorithmStep.MergeMove(
                                        currentIndex = i,
                                        newIndex = k,
                                        offsetCount = offsetCount
                                    ),
                                    SortingAlgorithmStep.Unselect(
                                        indices = intArrayOf(i, j),
                                    )
                                ),
                                title = resources.getString(R.string.merge_sort_number_is_smaller_new_position_was_found, array[i])
                            )
                        )

                        temporaryArray[k] = array[i]
                        i++
                    } else {
                        steps.add(
                            SortingAlgorithmStep.List(
                                steps = arrayOf(
                                    SortingAlgorithmStep.MergeMove(
                                        currentIndex = j,
                                        newIndex = k,
                                        offsetCount = offsetCount
                                    ),
                                    SortingAlgorithmStep.Unselect(
                                        indices = intArrayOf(i, j),
                                    )
                                ),
                                title = resources.getString(R.string.merge_sort_number_is_smaller_new_position_was_found, array[j])
                            )
                        )

                        temporaryArray[k] = array[j]
                        j++
                    }
                    k++
                }

                while (i < middle) {
                    steps.add(
                        SortingAlgorithmStep.MergeMove(
                            currentIndex = i,
                            newIndex = k,
                            offsetCount = offsetCount,
                            title = resources.getString(R.string.merge_sort_moving_number_to_correct_position, array[i])
                        )
                    )

                    temporaryArray[k] = array[i]
                    i++
                    k++
                }

                while (j < right) {
                    steps.add(
                        SortingAlgorithmStep.MergeMove(
                            currentIndex = j,
                            newIndex = k,
                            offsetCount = offsetCount,
                            title = resources.getString(R.string.merge_sort_moving_number_to_correct_position, array[j])
                        )
                    )

                    temporaryArray[k] = array[j]
                    j++
                    k++
                }

                i = left
                while (i < right) {
                    array[i] = temporaryArray[i]
                    i++
                }

                left += windowSize * 2
            }

            steps.add(SortingAlgorithmStep.Merge(
                title = resources.getString(R.string.merge_sort_sub_arrays_were_successfully_merged)
            ))

            windowSize *= 2
        }

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))
        return steps
    }

    private fun calcOffsetCount(currentIndex: Int, newIndex: Int, windowSize: Int, arraySize: Int): Int {
        val boundaryIndex = if (arraySize.mod(2) == 1) arraySize / 2 + 1 else arraySize / 2

        val absoluteDifference = (boundaryIndex - newIndex).absoluteValue
        val reducedOffsetCount = if (absoluteDifference == 1) 0 else absoluteDifference / windowSize

        return if (currentIndex < boundaryIndex) {
            -reducedOffsetCount
        } else {
            reducedOffsetCount
        }
    }

}