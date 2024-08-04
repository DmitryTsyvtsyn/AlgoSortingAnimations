package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

import android.content.res.Resources
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmStep

interface SortingAlgorithm {
    val title: Int
    val description: Int

    val worstTimeComplexity: String
    val bestTimeComplexity: String
    val averageTimeComplexity: String
    val worstSpaceComplexity: String

    fun sort(array: IntArray, resources: Resources): List<SortingAlgorithmStep>
}