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
                SortingAlgorithmStep.Select(
                    indices = intArrayOf(i),
                    titleResource = "Выбираем минимальное значение для позиции $i"
                )
            )
            var min = i
            for (j in i + 1 until arraySize) {
                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(j),
                        titleResource = "Сравниваем текущее минимальное значение ${array[min]} со значением ${array[j]}"
                    )
                )
                if (array[min] > array[j]) {
                    min = j
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j),
                            titleResource = "Значение ${array[j]} меньше текущего минимума, поэтому оно теперь минимальное"
                        )
                    )
                } else {
                    steps.add(
                        SortingAlgorithmStep.Unselect(
                            indices = intArrayOf(j),
                            titleResource = "Значение ${array[j]} больше текущего минимума, поэтому ничего не делаем"
                        )
                    )
                }
            }

            if (min != i) {

                steps.add(
                    SortingAlgorithmStep.Select(
                        indices = intArrayOf(min),
                        titleResource = "Нашли новое минимальное значение ${array[min]} для позиции $i, поэтому меняем местами"
                    )
                )
                steps.add(
                    SortingAlgorithmStep.Swap(
                        index1 = i,
                        index2 = min,
                        titleResource = "Нашли новое минимальное значение ${array[min]} для позиции $i, поэтому меняем местами"
                    )
                )

                val tmp = array[i]
                array[i] = array[min]
                array[min] = tmp

                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i, min),
                        titleResource = "Значение ${array[min]} на позиции $i является минимальным"
                    )
                )
            } else {
                steps.add(
                    SortingAlgorithmStep.Unselect(
                        indices = intArrayOf(i),
                        titleResource = "Значение ${array[i]} на позиции $i является минимальным"
                    )
                )
            }
        }

        steps.add(SortingAlgorithmStep.End(resources.getString(R.string.array_was_sorted_0)))

        return steps
    }


}