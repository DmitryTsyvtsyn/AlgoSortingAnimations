package io.github.dmitrytsyvtsyn.algosortinganimations.new_array

class SortingNewArrayState(
    val size: Int = 0,
    val sizes: IntArray = intArrayOf(),
    val array: IntArray = intArrayOf(),
    val backNavigated: Boolean = false,
    private val compared: Int = 15 // low byte: 00001111
) {
    
    fun hasChanged(pieceState: Int): Boolean = (compared and pieceState) == pieceState
    
    fun difference(other: SortingNewArrayState): SortingNewArrayState {
        var compared = 0

        if (size != other.size) {
            compared = compared or sizeChanged
        }

        if (!sizes.contentEquals(other.sizes)) {
            compared = compared or sizesChanged
        }

        if (!array.contentEquals(other.array)) {
            compared = compared or arrayChanged
        }

        if (backNavigated != other.backNavigated) {
            compared = compared or backNavigatedChanged
        }

        return SortingNewArrayState(size, sizes, array, backNavigated, compared)
    }
    
    fun changedWith(size: Int): SortingNewArrayState {
        val newArray = IntArray(size) { 0 }
        var index = 0
        while (index < size && index < array.size) {
            newArray[index] = array[index]
            index++
        }

        return SortingNewArrayState(size, sizes, newArray, backNavigated, compared)
    }

    fun changedWith(array: IntArray) = SortingNewArrayState(size, sizes, array, backNavigated, compared)

    fun changedWith(backNavigated: Boolean) = SortingNewArrayState(size, sizes, array, backNavigated, compared)

    companion object {
        const val sizeChanged: Int = 1 // low byte: 00000001
        const val sizesChanged: Int = 2 // low byte: 00000010
        const val arrayChanged: Int = 4 // low byte: 00000100
        const val backNavigatedChanged: Int = 8 // low byte: 00001000
    }
}