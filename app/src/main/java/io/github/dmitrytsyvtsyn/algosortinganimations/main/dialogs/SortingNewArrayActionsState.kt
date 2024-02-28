package io.github.dmitrytsyvtsyn.algosortinganimations.main.dialogs

class SortingNewArrayActionsState(
    val size: Int = 0,
    val sizes: IntArray = intArrayOf(),
    val array: IntArray = intArrayOf(),
    val sizesChanged: Boolean = false
) {

    fun copyWithChangedSize(size: Int): SortingNewArrayActionsState {

        var newArray = array
        if (array.size != size) {

            newArray = IntArray(size) { 0 }

            var index = 0
            while (index < size && index < array.size) {
                newArray[index] = array[index]
                index++
            }
        }

        return SortingNewArrayActionsState(
            size = size,
            sizes = sizes,
            array = newArray,
            sizesChanged = false
        )
    }

    fun copyWithChangedArray(array: IntArray) = SortingNewArrayActionsState(
        size = size,
        sizes = sizes,
        array = array,
        sizesChanged = false
    )

}