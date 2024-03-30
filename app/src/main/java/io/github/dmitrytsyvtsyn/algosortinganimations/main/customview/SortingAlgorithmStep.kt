package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

sealed class SortingAlgorithmStep(
    val title: String,
    val force: Boolean
) {

    class Swap(
        val index1: Int,
        val index2: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Swap(index1=$index1, index2=$index2, titleResource=$title)"
    }

    class Select(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Select(indices=${indices.toList()}, titleResource=$title)"
    }

    class Unselect(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
     ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Unselect(indices=${indices.toList()}, titleResource=$title)"
    }

    class SelectRange(
        val startIndex: Int,
        val endIndex: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "SelectRange(startIndex=$startIndex, endIndex=$endIndex, titleResource=$title)"
    }

    class UnselectRange(
        val startIndex: Int,
        val endIndex: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "UnselectRange(startIndex=$startIndex, endIndex=$endIndex, titleResource=$title)"
    }

    class End(
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "End(titleResource=$title)"
    }

    class List(
        val steps: Array<SortingAlgorithmStep>,
        title: String = ""
    ) : SortingAlgorithmStep(title, force = false) {

        override fun toString() = steps.joinToString(separator = ", ", prefix = "[ ", postfix = " ]")
    }

    object Empty : SortingAlgorithmStep("", force = false)  {
        override fun toString() = "Empty"
    }

}