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

    class InsertShift(
        val currentIndex: Int,
        val newIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "InsertShift(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    class InsertUp(
        val indices: IntArray,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "InsertUp(indices=${indices.toList()}, title=$title, force=$force)"
    }

    class Merge(
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Merge(title=$title, force=$force)"
    }

    class MergeDivide(
        val pivotIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "MergeDivide(pivotIndex=$pivotIndex, title=$title, force=$force)"
    }

    class MergeMove(
        val currentIndex: Int,
        val newIndex: Int,
        val offsetCount: Int = 0,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "MergeMove(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    class Quick(
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "Quick(title=$title, force=$force)"
    }

    class QuickDown(
        val currentIndex: Int,
        val newIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "QuickDown(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    class QuickMove(
        val currentIndex: Int,
        val newIndex: Int,
        title: String = "",
        force: Boolean = false
    ) : SortingAlgorithmStep(title, force) {

        override fun toString() =
            "QuickMove(currentIndex=$currentIndex, newIndex=$newIndex, title=$title, force=$force)"
    }

    object Empty : SortingAlgorithmStep("", force = false)  {
        override fun toString() = "Empty"
    }

}