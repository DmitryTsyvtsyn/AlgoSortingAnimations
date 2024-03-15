package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

class EmptyAlgorithm : SortingAlgorithm {

    override val title = -1
    override val description = -1

    override val worstTimeComplexity = ""
    override val bestTimeComplexity = ""
    override val averageTimeComplexity = ""
    override val worstSpaceComplexity = ""

    override fun sort(array: IntArray, resources: Resources) = emptyList<SortingAlgorithmStep>()

}