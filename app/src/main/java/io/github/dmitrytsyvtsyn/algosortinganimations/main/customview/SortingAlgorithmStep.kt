package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

sealed class SortingAlgorithmStep(val title: String, val force: Boolean) {

    class Swap(
        val index1: Int,
        val index2: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Swap(index1=$index1, index2=$index2, title=$title, force=$force)"
    }

    class Select(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Select(indices=${indices.toList()}, title=$title, force=$force)"
    }

    class Unselect(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
     ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "Unselect(indices=${indices.toList()}, title=$title, force=$force)"
    }

    class SelectRange(
        val startIndex: Int,
        val endIndex: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "SelectRange(startIndex=$startIndex, endIndex=$endIndex, title=$title, force=$force)"
    }

    class UnselectRange(
        val startIndex: Int,
        val endIndex: Int,
        title: String = "",
        force: Boolean = false
    ): SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "UnselectRange(startIndex=$startIndex, endIndex=$endIndex, title=$title, force=$force)"
    }

    class End(
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString(): String =
            "End(title=$title, force=$force)"
    }

    class List(
        val steps: Array<SortingAlgorithmStep>,
        title: String = ""
    ) : SortingAlgorithmStep(title, force = false) {

        override fun toString() =
            "List(steps=${steps.joinToString(separator = ", ", prefix = "[ ", postfix = " ]")}, title=$title)"
    }

    class Insert(
        val currentIndex: Int,
        val newIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Insert(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    class Shift(
        val currentIndex: Int,
        val newIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Shift(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    class Up(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Up(indices=${indices.toList()}, title=$title, force=$force)"
    }

    class Divide(
        val pivotIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Divide(pivotIndex=$pivotIndex, title=$title, force=$force)"
    }

    object Empty : SortingAlgorithmStep("", force = false)  {
        override fun toString() = "Empty"
    }

}