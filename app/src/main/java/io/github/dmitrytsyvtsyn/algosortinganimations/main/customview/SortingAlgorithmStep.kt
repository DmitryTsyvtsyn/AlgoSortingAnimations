package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

sealed class SortingAlgorithmStep(val title: String) {

    class Swap(val index1: Int, val index2: Int, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Swap(index1=$index1, index2=$index2, titleResource=$title)"
    }

    class Select(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Select(indices=${indices.toList()}, titleResource=$title)"
    }

    class Unselect(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Unselect(indices=${indices.toList()}, titleResource=$title)"
    }

    class SelectRange(val startIndex: Int, val endIndex: Int, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "SelectRange(startIndex=$startIndex, endIndex=$endIndex, titleResource=$title)"
    }

    class UnselectRange(val startIndex: Int, val endIndex: Int, title: String = ""): SortingAlgorithmStep(title) {

        override fun toString(): String =
            "UnselectRange(startIndex=$startIndex, endIndex=$endIndex, titleResource=$title)"
    }

    class End(titleResource: String = "") : SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "End(titleResource=$title)"
    }

    object Empty : SortingAlgorithmStep("")  {
        override fun toString() = "Empty"
    }

}