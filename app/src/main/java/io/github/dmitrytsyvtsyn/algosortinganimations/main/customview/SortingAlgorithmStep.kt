package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

sealed class SortingAlgorithmStep(val titleResource: String) {

    class Swap(val index1: Int, val index2: Int, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Swap(index1=$index1, index2=$index2, titleResource=$titleResource)"
    }

    class Select(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Select(indices=$indices, titleResource=$titleResource)"
    }

    class Unselect(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "Unselect(indices=$indices, titleResource=$titleResource)"
    }

    class End(titleResource: String = "") : SortingAlgorithmStep(titleResource) {

        override fun toString(): String =
            "End(titleResource=$titleResource)"
    }

}