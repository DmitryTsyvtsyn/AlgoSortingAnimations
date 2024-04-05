package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class InsertionSortAlgorithm : SortingAlgorithm {

    override val title = R.string.insertion_sort
    override val description = R.string.insertion_sort

    override val worstTimeComplexity = "O(n²)"
    override val bestTimeComplexity = "O(n)"
    override val averageTimeComplexity = "O(n²)"
    override val worstSpaceComplexity = "O(1)"

    override fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep> {
        val steps = mutableListOf<SortingAlgorithmStep>()

        val arraySize = array.size
        for (i in 1 until arraySize) {

            steps.add(
                SortingAlgorithmStep.SelectRange(
                    startIndex = 0,
                    endIndex = i,
                    title = resources.getString(R.string.insertion_sort_selecting_sub_array)
                )
            )

            val current = array[i]
            var j = i - 1

            steps.add(
                SortingAlgorithmStep.List(
                    steps = arrayOf(
                        SortingAlgorithmStep.Select(indices = intArrayOf(i)),
                        SortingAlgorithmStep.Up(indices = intArrayOf(i))
                    ),
                    title = resources.getString(R.string.insertion_sort_looking_for_new_position_for_number, current)
                )
            )

            steps.add(
                SortingAlgorithmStep.Select(
                    indices = intArrayOf(j),
                    title = resources.getString(R.string.insertion_sort_comparing_numbers, current, array[j])
                )
            )

            while (true) {

                if (j < 0) break

                if (array[j] < current) {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j),
                            title = resources.getString(R.string.insertion_sort_nothing_to_do_numbers_was_already_ordered, current, array[j])
                        )
                    )

                    break
                }

                steps.add(
                    SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.Shift(
                                currentIndex = j,
                                newIndex = j + 1
                            ),
                            SortingAlgorithmStep.Unselect(
                                indices = intArrayOf(j + 1)
                            )
                        ),
                        title = resources.getString(R.string.insertion_sort_shifting_number, array[j], current)
                    )
                )

                array[j + 1] = array[j]

                j--
            }

            if (j + 1 != i) {
                array[j + 1] = current

                steps.add(
                    SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.Move(
                                currentIndex = i,
                                newIndex = j + 1,
                            ),
                            SortingAlgorithmStep.Unselect(
                                indices = intArrayOf(j + 1)
                            )
                        ),
                        title = resources.getString(R.string.insertion_sort_new_position_was_found, current)
                    )
                )
            } else {
                steps.add(
                    SortingAlgorithmStep.List(
                        steps = arrayOf(
                            SortingAlgorithmStep.Move(
                                currentIndex = i,
                                newIndex = i,
                            ),
                            SortingAlgorithmStep.Unselect(
                                indices = intArrayOf(i)
                            )
                        ),
                        title = resources.getString(R.string.insertion_sort_position_was_not_changed, current)
                    )
                )
            }

            steps.add(
                SortingAlgorithmStep.UnselectRange(
                    startIndex = 0,
                    endIndex = i,
                    title = resources.getString(R.string.insertion_sort_sub_array_was_sorted)
                )
            )
        }

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted)))

        return steps
    }

}