package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

sealed class SortingAlgorithmStep(val titleResource: String) {

    abstract fun modifyArray(array: IntArray)

    class Swap(val index1: Int, val index2: Int, titleResource: String = ""): SortingAlgorithmStep(titleResource) {
        override fun modifyArray(array: IntArray) {
            val tmp = array[index1]
            array[index1] = array[index2]
            array[index2] = tmp
        }
    }

    class Select(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {
        override fun modifyArray(array: IntArray) {
            // nothing to modify
        }
    }

    class Unselect(val indices: IntArray, titleResource: String = ""): SortingAlgorithmStep(titleResource) {
        override fun modifyArray(array: IntArray) {
            // nothing to modify
        }
    }

    class End(titleResource: String = "") : SortingAlgorithmStep(titleResource) {
        override fun modifyArray(array: IntArray) {
            // nothing to modify
        }
    }

}